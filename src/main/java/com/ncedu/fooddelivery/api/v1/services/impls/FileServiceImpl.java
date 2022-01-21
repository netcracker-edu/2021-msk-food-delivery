package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.file.FileInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.file.FileLinkDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.UserInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.File;
import com.ncedu.fooddelivery.api.v1.entities.FileType;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.BadFileExtensionException;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.FileDeleteException;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.FileStorageException;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAccessDeniedException;
import com.ncedu.fooddelivery.api.v1.repos.FileRepo;
import com.ncedu.fooddelivery.api.v1.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${file.upload.location}")
    private String UPLOAD_LOCATION;

    @Autowired
    FileRepo fileRepo;

    @Override
    public FileLinkDTO save(MultipartFile file, User owner) {
        try {
            String originalFileName = file.getOriginalFilename();
            String fileName = getFileNameWithoutExt(originalFileName);
            FileType fileType = getFileType(originalFileName);
            Long fileSize = file.getSize();
            UUID fileUuid = UUID.randomUUID();

            Path fullPathToFile = createFullPathToFile(fileUuid);
            Files.copy(file.getInputStream(), fullPathToFile, StandardCopyOption.REPLACE_EXISTING);

            File fileEntity = new File(fileUuid, fileType, fileName, fileSize,
                                        Timestamp.valueOf(LocalDateTime.now()), owner);
            fileRepo.save(fileEntity);

            String fileLink = createFileLink(fileUuid);
            return new FileLinkDTO(fileLink, fileUuid.toString());

        } catch (BadFileExtensionException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileStorageException();
        }
    }

    private String getFileNameWithoutExt(String originalFileName) {
        int lastDotIndex = getLastDotIndex(originalFileName);
        return originalFileName.substring(0, lastDotIndex);
    }

    private FileType getFileType(String fileName) {
        int lastDotIndex = getLastDotIndex(fileName);
        String ext = fileName.substring(lastDotIndex+1);
        String upperExt = ext.toUpperCase();
        upperExt =  upperExt.equals("JPG") ? "JPEG" : upperExt;
        if (!FileType.isCorrectExt(upperExt)) {
            throw new BadFileExtensionException();
        }
        return FileType.valueOf(upperExt);
    }

    private int getLastDotIndex(String fileName) {
        return fileName.lastIndexOf('.');
    }

    private Path createFullPathToFile(UUID fileUuid) throws IOException {
        String fileUuidString = fileUuid.toString();
        String fileParentDir = fileUuidString.substring(0, 2); // extract 2 chars from file UUID
        Path uploadPath = Paths.get(UPLOAD_LOCATION).toAbsolutePath().normalize();
        Path fileParentDirPath = uploadPath.resolve(fileParentDir);
        Files.createDirectories(fileParentDirPath);
        return fileParentDirPath.resolve(fileUuidString);
    }

    private String createFileLink(UUID fileUuid) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/file/")
                .path(fileUuid.toString())
                .toUriString();
    }

    @Override
    public Resource load(File file) {
        try {
            Path fullFilePath = createFullPathToFile(file.getId());
            Resource resource = new UrlResource(fullFilePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            throw new NotFoundEx(file.getId().toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NotFoundEx(file.getId().toString());
        }
    }

    @Override
    public void delete(File file, User authedUser) {

        checkAdminOrOwner(file, authedUser);

        try {
            fileRepo.delete(file);
            Path fullFilePath = createFullPathToFile(file.getId());
            Files.deleteIfExists(fullFilePath);
            Path fileParentDirPath = fullFilePath.getParent();
            log.debug("PARENT DIR PATH: " + fileParentDirPath + "\n");
            boolean isParentDirEmpty = checkParentDirEmpty(fileParentDirPath);
            if (isParentDirEmpty) {
                Files.delete(fileParentDirPath);
            }
        } catch (IOException e) {
            throw new FileDeleteException();
        }
    }

    private void checkAdminOrOwner(File file, User authedUser) {
        Long fileOwnerId = file.getOwner().getId();
        boolean isNotAdmin = !Role.isADMIN(authedUser.getRole());
        boolean isNotOwner = !fileOwnerId.equals(authedUser.getId());
        if (isNotAdmin && isNotOwner) {
            log.error(authedUser.getEmail() + " not Admin and not Owner of the file " + file.getId().toString());
            throw new CustomAccessDeniedException();
        }
    }

    private boolean checkParentDirEmpty(Path fileParentDirPath) throws IOException {
        if (Files.isDirectory(fileParentDirPath)) {
            try(Stream<Path> entries = Files.list(fileParentDirPath)) {
                return !entries.findFirst().isPresent();
            }
        }
        return false;
    }

    @Override
    public FileLinkDTO replace(MultipartFile newFile, File oldFile, User authedUser) {

        checkAdminOrOwner(oldFile, authedUser);

        try {
            String originalFileName = newFile.getOriginalFilename();
            String newFileName = getFileNameWithoutExt(originalFileName);
            FileType fileType = getFileType(originalFileName);
            Long fileSize = newFile.getSize();

            Path fullPathToFile = createFullPathToFile(oldFile.getId());
            Files.copy(newFile.getInputStream(), fullPathToFile, StandardCopyOption.REPLACE_EXISTING);

            oldFile.setName(newFileName);
            oldFile.setType(fileType);
            oldFile.setSize(fileSize);
            oldFile.setUploadDate(Timestamp.valueOf(LocalDateTime.now()));
            fileRepo.save(oldFile);
            String fileLink = createFileLink(oldFile.getId());
            return new FileLinkDTO(fileLink, oldFile.getId().toString());
        } catch (BadFileExtensionException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileStorageException();
        }
    }

    @Override
    public List<FileInfoDTO> getAllFiles(Pageable pageable) {
        Iterable<File> files = fileRepo.findAll(pageable);
        Iterator<File> iterator = files.iterator();
        List<FileInfoDTO> filesDTO= new ArrayList<>();
        while (iterator.hasNext()) {
            filesDTO.add(createFileDTO(iterator.next()));
        }
        return filesDTO;
    }

    private FileInfoDTO createFileDTO(File file) {
        String fileLink = createFileLink(file.getId());
        return new FileInfoDTO(file.getId().toString(), file.getType().getMediaType(),
                                file.getName(), file.getSize(),
                                file.getUploadDate(), fileLink,
                                createUserDTO(file.getOwner()));
    }

    private UserInfoDTO createUserDTO(User user) {
        return new UserInfoDTO(user.getId(), user.getRole().name(),
                user.getFullName(), user.getEmail(),
                user.getLastSigninDate(), user.getAvatarId());
    }

}
