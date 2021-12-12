package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.entities.File;
import com.ncedu.fooddelivery.api.v1.entities.FileType;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.repos.FileRepo;
import com.ncedu.fooddelivery.api.v1.services.FileService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
            String ext = originalName.substring(lastDotIndex);
            String fileName = originalName.substring(0, lastDotIndex);
            //TODO check extension of file
            //TODO transform string ext to FileType ENUM

            Long fileSize = file.getSize();
            UUID fileUUID = UUID.randomUUID();

            String fileUUIDString = fileUUID.toString();
            String fileParentDir = fileUUIDString.substring(0, 2); // extract 2 chars from file UUID
            Path uploadPath = Paths.get(uploadLocation).toAbsolutePath().normalize();

            Path fileParentDirPath = uploadPath.resolve(fileParentDir);
            Files.createDirectories(fileParentDirPath);

            Path fullPathToFile = fileParentDirPath.resolve(fileUUIDString);
            Files.copy(file.getInputStream(), fullPathToFile, StandardCopyOption.REPLACE_EXISTING);

            File fileEntity = new File(fileUUID, FileType.JPEG, fileName, fileSize, Timestamp.valueOf(LocalDateTime.now()), owner);
            fileRepo.save(fileEntity);
            return fileUUIDString;

        } catch (Exception e) {// TODO write own errors for proccessing
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Resource load(String fileUUID) {
        try {
            String fileUUIDString = fileUUID.toString();
            String fileParentDir = fileUUIDString.substring(0, 2); // extract 2 chars from file UUID
            Path uploadPath = Paths.get(uploadLocation).toAbsolutePath().normalize();
            Path fullFilePath = uploadPath.resolve(fileParentDir).resolve(fileUUIDString);
            Resource resource = new UrlResource(fullFilePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }

            throw new NotFoundEx(fileUUID);
        } catch (MalformedURLException e) {
            throw new NotFoundEx(fileUUID);
        }
    }
}
