package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.entities.File;
import com.ncedu.fooddelivery.api.v1.entities.FileType;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.BadFileExtensionException;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.FileDeleteException;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.FileStorageException;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.repos.FileRepo;
import com.ncedu.fooddelivery.api.v1.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${file.upload.location}")
    private String uploadLocation;

    @Autowired
    FileRepo fileRepo;

    @Override
    public String save(MultipartFile file, User owner) {
        try {
            //TODO: refactor code
            String originalName = file.getOriginalFilename();
            int lastDotIndex = originalName.lastIndexOf('.');
            String ext = originalName.substring(lastDotIndex+1).toUpperCase();
            String fileName = originalName.substring(0, lastDotIndex);
            if (ext != null && ext.equals("JPG")) {
                ext = "JPEG";
            }
            if (!FileType.isCorrectExt(ext)) {
                throw new BadFileExtensionException();
            }
            FileType fileType = FileType.valueOf(ext);
            String fileMediaType = fileType.getMediaType();

            Long fileSize = file.getSize();
            UUID fileUUID = UUID.randomUUID();

            String fileUUIDString = fileUUID.toString();
            String fileParentDir = fileUUIDString.substring(0, 2); // extract 2 chars from file UUID
            Path uploadPath = Paths.get(uploadLocation).toAbsolutePath().normalize();

            Path fileParentDirPath = uploadPath.resolve(fileParentDir);
            Files.createDirectories(fileParentDirPath);

            Path fullPathToFile = fileParentDirPath.resolve(fileUUIDString);
            Files.copy(file.getInputStream(), fullPathToFile, StandardCopyOption.REPLACE_EXISTING);

            File fileEntity = new File(fileUUID, fileType, fileName, fileSize, Timestamp.valueOf(LocalDateTime.now()), owner);
            fileRepo.save(fileEntity);
            return fileUUIDString;

        } catch (BadFileExtensionException e) {
            throw e;
        }
        catch (Exception e) {
            throw new FileStorageException();
        }
    }

    @Override
    public Resource load(File file) {
        String fileUUIDString = file.getId().toString();
        try {
            String fileParentDir = fileUUIDString.substring(0, 2); // extract 2 chars from file UUID
            Path uploadPath = Paths.get(uploadLocation).toAbsolutePath().normalize();
            Path fullFilePath = uploadPath.resolve(fileParentDir).resolve(fileUUIDString);
            Resource resource = new UrlResource(fullFilePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            throw new NotFoundEx(fileUUIDString);
        } catch (MalformedURLException e) {
            throw new NotFoundEx(fileUUIDString);
        }
    }

    @Override
    public void delete(File file) {
        String fileUUIDString = file.getId().toString();
        try {
            fileRepo.delete(file);
            Path uploadPath = Paths.get(uploadLocation).toAbsolutePath().normalize();
            String fileParentDir = fileUUIDString.substring(0, 2); // extract 2 chars from file UUID
            Path fullFilePath = uploadPath.resolve(fileParentDir).resolve(fileUUIDString);
            Files.deleteIfExists(fullFilePath);
            //TODO: delete parent folder if empty
        } catch (IOException e) {
            throw new FileDeleteException();
        }
    }

}
