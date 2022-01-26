package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.file.FileLinkDTO;
import com.ncedu.fooddelivery.api.v1.entities.File;
import com.ncedu.fooddelivery.api.v1.entities.FileType;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.repos.FileRepo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = { "file.upload.location=./testfiles" })
@Slf4j
public class FileServiceTest {

    @MockBean
    private FileRepo fileRepoMock;

    @Autowired
    private FileService fileService;

    @Value("${file.upload.location}")
    private String UPLOAD_LOCATION;

    @BeforeAll
    public static void createPictures() throws IOException{
        createPNG();
        createJPG();
    }

    private static void createPNG() throws IOException {
        String fileName = "test.png";
        Path path = Paths.get("")
                .toAbsolutePath();
        Path filePath = path.resolve(fileName);
        BufferedImage outputImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(outputImage, 0, 0, Color.WHITE, null);
        ImageIO.write(outputImage, "png", filePath.toFile());
    }

    private static void createJPG() throws IOException {
        String fileName = "test.jpg";
        Path path = Paths.get("")
                .toAbsolutePath();
        Path filePath = path.resolve(fileName);
        BufferedImage outputImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(outputImage, 0, 0, Color.WHITE, null);
        ImageIO.write(outputImage, "jpg", filePath.toFile());
    }

    @Test
    public void savePngAdmin() throws IOException {
        Long userId = 1L;
        User sheldonAdmin = UserUtils.adminSheldonCooper(userId);
        String fileName = "test.png";
        String contentType = "image/png";
        Path path = Paths.get("")
                    .toAbsolutePath();
        Path filePath = path.resolve(fileName);
        MultipartFile file = new MockMultipartFile(fileName, fileName, contentType, Files.readAllBytes(filePath));
        FileType fileType = FileType.PNG;
        Long fileSize = Files.size(filePath);
        log.info("FILE SIZE before uploading: " + fileSize + " fileName " + fileName);
        when(fileRepoMock.save(any(File.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        FileLinkDTO fileLinkDTO = fileService.save(file, sheldonAdmin);
        String fileUuid = fileLinkDTO.getFileUuid();
        log.info("FILE UUID: " + fileUuid);
        String parentDir = fileUuid.substring(0, 2);
        Path upload = Paths.get(UPLOAD_LOCATION).toAbsolutePath().normalize();
        Path uploadedFile = upload.resolve(parentDir).resolve(fileUuid);
        log.info("FILE SIZE after uploading: " + Files.size(uploadedFile));
        assertTrue(Files.exists(uploadedFile));
    }

}
