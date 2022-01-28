package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.file.FileInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.file.FileLinkDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.UserInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.File;
import com.ncedu.fooddelivery.api.v1.entities.FileType;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.BadFileExtensionException;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAccessDeniedException;
import com.ncedu.fooddelivery.api.v1.repos.FileRepo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = { "file.upload.location=./testfiles" })
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileServiceTest {

    @MockBean
    private FileRepo fileRepoMock;

    @Autowired
    private FileService fileService;

    @Value("${file.upload.location}")
    private String UPLOAD_LOCATION;

    @Value("${file.client.image.width}")
    private int CLIENT_IMAGE_WIDTH;

    @Value("${file.client.image.height}")
    private int CLIENT_IMAGE_HEIGHT;

    private Path UPLOAD_PATH;
    private final Map<String, Integer[]> pictureNamesAndSize =  new HashMap<>()
                {{
                    put("test", new Integer[] {200, 200});
                    put("testLargeHeight", new Integer[] {900, 1400});
                    put("testLargeWidth", new Integer[] {1400, 900});
                }};

    @BeforeAll
    public void createPictures() throws IOException {
        UPLOAD_PATH = Paths.get(UPLOAD_LOCATION).toAbsolutePath().normalize();
        Files.createDirectories(UPLOAD_PATH);
        pictureNamesAndSize.entrySet().forEach(this::createPngJpgBmp);
    }

    private void createPngJpgBmp(Map.Entry<String, Integer[]> entry) {
        final String JPEG_EXT = "jpeg";
        processPicture(entry, JPEG_EXT);
        final String PNG_EXT = "png";
        processPicture(entry, PNG_EXT);
        final String BMP_EXT = "bmp";
        processPicture(entry, BMP_EXT);
    }

    private void processPicture(Map.Entry<String, Integer[]> entry, String EXT) {
        String fileName = entry.getKey();
        int width = entry.getValue()[0];
        int height = entry.getValue()[1];
        String fileNameWithExt = fileName+"."+EXT;
        Path filePath = UPLOAD_PATH.resolve(fileNameWithExt);
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(outputImage, 0, 0, Color.WHITE, null);
        savePicture(outputImage, EXT, filePath);
    }

    private void savePicture(BufferedImage outputImage, String EXT, Path filePath) {
        try {
            ImageIO.write(outputImage, EXT, filePath.toFile());
        } catch (Exception e) {
            new RuntimeException(e);
        }
    }

    @AfterAll
    public void deletePictures() throws  IOException {
        cleanTestDir(UPLOAD_PATH.toFile());
    }

    private void cleanTestDir(java.io.File dir) {
        for (java.io.File file: dir.listFiles()) {
            if (file.isDirectory())
                cleanTestDir(file);
            file.delete();
        }
    }

    @Test
    public void savePngAdmin() throws IOException {
        //prepare
        Long userId = 1L;
        User sheldonAdmin = UserUtils.adminSheldonCooper(userId);

        MultipartFile file = getImgWithType("test", "png");
        Long fileSize = file.getSize();
        log.info("FILE SIZE before uploading: " + fileSize + " FILE NAME: " + file.getName());

        when(fileRepoMock.save(any(File.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        FileLinkDTO fileLinkDTO = fileService.save(file, sheldonAdmin);

        Path uploadedFile = getUploadedFilePath(fileLinkDTO.getFileUuid());
        assertTrue(Files.exists(uploadedFile));
        Long uploadedFileSize = Files.size(uploadedFile);
        log.info("FILE SIZE after uploading: " + uploadedFileSize + " FILE UUID: " + fileLinkDTO.getFileUuid());
        assertEquals(fileSize, uploadedFileSize);
        verify(fileRepoMock, times(1)).save(any(File.class));
    }

    @Test
    public void saveJpegLargeHeightAdmin() throws IOException {
        //prepare
        Long userId = 1L;
        User sheldonAdmin = UserUtils.adminSheldonCooper(userId);

        MultipartFile file = getImgWithType("testLargeHeight", "jpeg");
        Long fileSize = file.getSize();
        log.info("FILE SIZE before uploading: " + fileSize + " FILE NAME: " + file.getName());

        when(fileRepoMock.save(any(File.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        FileLinkDTO fileLinkDTO = fileService.save(file, sheldonAdmin);

        Path uploadedFile = getUploadedFilePath(fileLinkDTO.getFileUuid());
        assertTrue(Files.exists(uploadedFile));
        Long uploadedFileSize = Files.size(uploadedFile);
        log.info("FILE SIZE after uploading: " + uploadedFileSize + " FILE UUID: " + fileLinkDTO.getFileUuid());
        assertEquals(fileSize, uploadedFileSize);
        verify(fileRepoMock, times(1)).save(any(File.class));
    }

    @Test
    public void saveWithNotSupportedExt() {
        //prepare
        Long userId = 1L;
        User sheldonAdmin = UserUtils.adminSheldonCooper(userId);

        MultipartFile file = getImgWithType("test", "bmp");
        Long fileSize = file.getSize();
        log.info("FILE SIZE before uploading: " + fileSize + " FILE NAME: " + file.getName());

        when(fileRepoMock.save(any(File.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        Exception exception = assertThrows(BadFileExtensionException.class, () -> {
            fileService.save(file, sheldonAdmin);
        });
        String perfectMessage = new BadFileExtensionException().getMessage();
        String resultMessage = exception.getMessage();

        verify(fileRepoMock, never()).save(any(File.class));
        assertEquals(perfectMessage, resultMessage);
    }

    @Test
    public void saveJpegClient() throws IOException {
        //prepare
        Long userId = 1L;
        User rajeshClient = UserUtils.clientRajeshKoothrappali(userId);

        MultipartFile file = getImgWithType("test", "jpeg");
        Long fileSize = file.getSize();
        log.info("FILE SIZE before uploading: " + fileSize + " FILE NAME: " + file.getName());

        when(fileRepoMock.save(any(File.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        FileLinkDTO fileLinkDTO =  fileService.save(file, rajeshClient);
        Path uploadedFile = getUploadedFilePath(fileLinkDTO.getFileUuid());
        assertTrue(Files.exists(uploadedFile));
        Long uploadedFileSize = Files.size(uploadedFile);
        log.info("FILE SIZE after uploading: " + uploadedFileSize + " FILE UUID: " + fileLinkDTO.getFileUuid());
        assertEquals(fileSize, uploadedFileSize);
        verify(fileRepoMock, times(1)).save(any(File.class));
    }

    @Test
    public void saveJpegLargeHeightClient() throws IOException {
        //prepare
        Long userId = 1L;
        User rajeshClient = UserUtils.clientRajeshKoothrappali(userId);

        MultipartFile file = getImgWithType("testLargeHeight", "jpeg");
        Long fileSize = file.getSize();
        log.info("FILE SIZE before uploading: " + fileSize + " FILE NAME: " + file.getName());

        when(fileRepoMock.save(any(File.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        FileLinkDTO fileLinkDTO =  fileService.save(file, rajeshClient);
        verify(fileRepoMock, times(1)).save(any(File.class));

        Path uploadedFile = getUploadedFilePath(fileLinkDTO.getFileUuid());
        assertTrue(Files.exists(uploadedFile));

        Long uploadedFileSize = Files.size(uploadedFile);
        log.info("FILE SIZE after uploading: " + uploadedFileSize + " FILE UUID: " + fileLinkDTO.getFileUuid());
        //uploaded size less than income because convert from jpeg to jpeg with small resolution
        assertTrue(fileSize > uploadedFileSize);

        BufferedImage bufferedImage = ImageIO.read(uploadedFile.toFile());
        assertEquals(CLIENT_IMAGE_HEIGHT, bufferedImage.getHeight());
    }

    @Test
    public void saveJpegLargeWidthClient() throws IOException {
        //prepare
        Long userId = 1L;
        User rajeshClient = UserUtils.clientRajeshKoothrappali(userId);

        MultipartFile file = getImgWithType("testLargeWidth", "jpeg");
        Long fileSize = file.getSize();
        log.info("FILE SIZE before uploading: " + fileSize + " FILE NAME: " + file.getName());

        when(fileRepoMock.save(any(File.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        FileLinkDTO fileLinkDTO =  fileService.save(file, rajeshClient);
        verify(fileRepoMock, times(1)).save(any(File.class));

        Path uploadedFile = getUploadedFilePath(fileLinkDTO.getFileUuid());
        assertTrue(Files.exists(uploadedFile));

        Long uploadedFileSize = Files.size(uploadedFile);
        log.info("FILE SIZE after uploading: " + uploadedFileSize + " FILE UUID: " + fileLinkDTO.getFileUuid());
        //uploaded size less than income because convert from jpeg to jpeg with small resolution
        assertTrue(fileSize > uploadedFileSize);

        BufferedImage bufferedImage = ImageIO.read(uploadedFile.toFile());
        assertEquals(CLIENT_IMAGE_WIDTH, bufferedImage.getWidth());
    }

    @Test
    public void savePngClient() throws IOException {
        //prepare
        Long userId = 1L;
        User rajeshClient = UserUtils.clientRajeshKoothrappali(userId);

        MultipartFile file = getImgWithType("test", "png");
        Long fileSize = file.getSize();
        log.info("FILE SIZE before uploading: " + fileSize + " FILE NAME: " + file.getName());

        when(fileRepoMock.save(any(File.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        FileLinkDTO fileLinkDTO =  fileService.save(file, rajeshClient);
        verify(fileRepoMock, times(1)).save(any(File.class));

        Path uploadedFile = getUploadedFilePath(fileLinkDTO.getFileUuid());
        assertTrue(Files.exists(uploadedFile));

        Long uploadedFileSize = Files.size(uploadedFile);
        log.info("FILE SIZE after uploading: " + uploadedFileSize + " FILE UUID: " + fileLinkDTO.getFileUuid());
        //uploaded size more than income because convert from png to jpeg
        assertTrue(fileSize < uploadedFileSize);
    }

    @Test
    public void savePngLargeWidthClient() throws IOException {
        //prepare
        Long userId = 1L;
        User rajeshClient = UserUtils.clientRajeshKoothrappali(userId);

        MultipartFile file = getImgWithType("testLargeWidth", "png");
        Long fileSize = file.getSize();
        log.info("FILE SIZE before uploading: " + fileSize + " FILE NAME: " + file.getName());

        when(fileRepoMock.save(any(File.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        FileLinkDTO fileLinkDTO =  fileService.save(file, rajeshClient);
        verify(fileRepoMock, times(1)).save(any(File.class));

        Path uploadedFile = getUploadedFilePath(fileLinkDTO.getFileUuid());
        assertTrue(Files.exists(uploadedFile));

        Long uploadedFileSize = Files.size(uploadedFile);
        log.info("FILE SIZE after uploading: " + uploadedFileSize + " FILE UUID: " + fileLinkDTO.getFileUuid());
        //uploaded size more than income because convert from png to jpeg
        assertTrue(fileSize < uploadedFileSize);

        BufferedImage bufferedImage = ImageIO.read(uploadedFile.toFile());
        assertEquals(CLIENT_IMAGE_WIDTH, bufferedImage.getWidth());
    }

    @Test
    public void savePngLargeHeightClient() throws IOException {
        //prepare
        Long userId = 1L;
        User rajeshClient = UserUtils.clientRajeshKoothrappali(userId);

        MultipartFile file = getImgWithType("testLargeHeight", "png");
        Long fileSize = file.getSize();
        log.info("FILE SIZE before uploading: " + fileSize + " FILE NAME: " + file.getName());

        when(fileRepoMock.save(any(File.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        FileLinkDTO fileLinkDTO =  fileService.save(file, rajeshClient);
        verify(fileRepoMock, times(1)).save(any(File.class));

        Path uploadedFile = getUploadedFilePath(fileLinkDTO.getFileUuid());
        assertTrue(Files.exists(uploadedFile));

        Long uploadedFileSize = Files.size(uploadedFile);
        log.info("FILE SIZE after uploading: " + uploadedFileSize + " FILE UUID: " + fileLinkDTO.getFileUuid());
        //uploaded size more than income because convert from png to jpeg
        assertTrue(fileSize < uploadedFileSize);

        BufferedImage bufferedImage = ImageIO.read(uploadedFile.toFile());
        assertEquals(CLIENT_IMAGE_HEIGHT, bufferedImage.getHeight());
    }

    private MultipartFile getImgWithType(String name, String ext) {
        String imgName = name+"."+ext;
        return createMultipartFile(imgName);
    }

    private MultipartFile createMultipartFile(String imgName) {
        Path filePath = UPLOAD_PATH.resolve(imgName);
        return new MockMultipartFile(imgName, imgName,
                FileType.PNG.getMediaType(),
                getImgBytes(filePath));
    }

    private byte[] getImgBytes(Path filePath) {
        try {
           return Files.readAllBytes(filePath);
        } catch (Exception e) {
            new RuntimeException(e);
        }
        return null;
    }

    private Path getUploadedFilePath(String fileUuid) {
        String parentDir = fileUuid.substring(0, 2);
        return  UPLOAD_PATH.resolve(parentDir).resolve(fileUuid);
    }


    @Test
    public void downloadPng() throws IOException {
        String imgName = "test.png";
        Path pathTestPng = UPLOAD_PATH.resolve(imgName);
        //file name, where we locate our test.png
        final String fileUuid = "6f10ce6c-74b0-4ad1-a268-c2715b07b59d";
        Path fileUuidPath = createFileUuidPath(fileUuid);
        Files.copy(pathTestPng, fileUuidPath, StandardCopyOption.REPLACE_EXISTING);

        File fileEntity = new File();
        fileEntity.setId(UUID.fromString(fileUuid));

        Resource resultResource = fileService.load(fileEntity);
        assertEquals(fileUuid, resultResource.getFilename());
        Resource perfectResource = new UrlResource(fileUuidPath.toUri());
        assertEquals(perfectResource, resultResource);
    }

    @Test
    public void downloadJpeg() throws IOException {
        String imgName = "test.jpeg";
        Path pathTestPng = UPLOAD_PATH.resolve(imgName);
        //file name, where we locate our test.png
        final String fileUuid = "2e20bc55-82df-4978-8e6e-d1d86def7039";
        Path fileUuidPath = createFileUuidPath(fileUuid);
        Files.copy(pathTestPng, fileUuidPath, StandardCopyOption.REPLACE_EXISTING);

        File fileEntity = new File();
        fileEntity.setId(UUID.fromString(fileUuid));

        Resource resultResource = fileService.load(fileEntity);
        assertEquals(fileUuid, resultResource.getFilename());
        Resource perfectResource = new UrlResource(fileUuidPath.toUri());
        assertEquals(perfectResource, resultResource);
    }

    @Test
    public void downloadNotExistentFile() {
        final String fileUuid = "179173a2-cf35-4191-a383-46cf1892d9c0";
        File fileEntity = new File();
        fileEntity.setId(UUID.fromString(fileUuid));

        Exception exception = assertThrows(NotFoundEx.class, () -> {
            fileService.load(fileEntity);
        });
        String perfectMessage = new NotFoundEx(fileUuid).getMessage();
        String resultMessage = exception.getMessage();
        assertEquals(perfectMessage, resultMessage);
    }

    @Test
    public void deleteImgAdmin() throws IOException {
        //prepare
        Long adminId = 1L;
        User sheldonAdmin = UserUtils.adminSheldonCooper(adminId);
        Long ownerId = 2L;
        User howardOwner = UserUtils.courierHowardWolowitz(ownerId);

        String imgName = "test.jpeg";
        Path pathTestPng = UPLOAD_PATH.resolve(imgName);
        //file name, where we locate our test.png
        final String fileUuid = "e2233b13-8439-4cf4-a5a3-f3351ebe6368";
        Path fileUuidPath = createFileUuidPath(fileUuid);
        Files.copy(pathTestPng, fileUuidPath, StandardCopyOption.REPLACE_EXISTING);

        File fileEntity = new File(UUID.fromString(fileUuid), FileType.JPEG, imgName,
                Files.size(fileUuidPath), Timestamp.valueOf(LocalDateTime.now()), howardOwner);

        doNothing().when(fileRepoMock).delete(fileEntity);

        fileService.delete(fileEntity, sheldonAdmin);
        assertFalse(Files.exists(fileUuidPath));
        verify(fileRepoMock, times(1)).delete(fileEntity);
    }

    @Test
    public void deleteImgOwner() throws IOException {
        //prepare
        Long ownerId = 1L;
        User howardOwner = UserUtils.courierHowardWolowitz(ownerId);

        String imgName = "test.jpeg";
        Path pathTestPng = UPLOAD_PATH.resolve(imgName);
        //file name, where we locate our test.png
        final String fileUuid = "24816b73-f989-4c72-89b3-67450141dafc";
        Path fileUuidPath = createFileUuidPath(fileUuid);
        Files.copy(pathTestPng, fileUuidPath, StandardCopyOption.REPLACE_EXISTING);

        File fileEntity = new File(UUID.fromString(fileUuid), FileType.JPEG, imgName,
                Files.size(fileUuidPath), Timestamp.valueOf(LocalDateTime.now()), howardOwner);

        doNothing().when(fileRepoMock).delete(fileEntity);

        fileService.delete(fileEntity, howardOwner);
        assertFalse(Files.exists(fileUuidPath));
        verify(fileRepoMock, times(1)).delete(fileEntity);
    }

    @Test
    public void deleteImgNotAdminNotOwner() throws IOException {
        //prepare
        Long ownerId = 1L;
        User howardOwner = UserUtils.courierHowardWolowitz(ownerId);
        Long notOwnerId = 2L;
        User leonardNotOwner = UserUtils.moderatorLeonardHofstadter(notOwnerId);

        String imgName = "test.jpeg";
        Path pathTestPng = UPLOAD_PATH.resolve(imgName);
        //file name, where we locate our test.png
        final String fileUuid = "55420882-1e23-4559-b84e-03c3f4d597af";
        Path fileUuidPath = createFileUuidPath(fileUuid);
        Files.copy(pathTestPng, fileUuidPath, StandardCopyOption.REPLACE_EXISTING);

        File fileEntity = new File(UUID.fromString(fileUuid), FileType.JPEG, imgName,
                Files.size(fileUuidPath), Timestamp.valueOf(LocalDateTime.now()), howardOwner);

        doNothing().when(fileRepoMock).delete(fileEntity);

        Exception exception = assertThrows(CustomAccessDeniedException.class, () -> {
            fileService.delete(fileEntity, leonardNotOwner);
        });
        String perfectMessage = new CustomAccessDeniedException().getMessage();
        String resultMessage = exception.getMessage();
        assertEquals(perfectMessage, resultMessage);
        verify(fileRepoMock, never()).delete(fileEntity);
    }

    @Test
    public void replaceImgAdmin() throws IOException {
        //prepare
        Long userId = 1L;
        User sheldonAdmin = UserUtils.adminSheldonCooper(userId);

        String oldImgName = "test.png";
        int oldImgHeight = pictureNamesAndSize.get("test")[1];
        Path pathTestPng = UPLOAD_PATH.resolve(oldImgName);

        final String fileUuid = "7cdb597b-303e-41d2-8bcc-802ccaaa83a5";
        Path fileUuidPath = createFileUuidPath(fileUuid);
        Files.copy(pathTestPng, fileUuidPath, StandardCopyOption.REPLACE_EXISTING);
        Long oldFileSize = Files.size(fileUuidPath);

        MultipartFile newFile = getImgWithType("testLargeHeight", "jpeg");

        File oldFileEntity = new File(UUID.fromString(fileUuid), FileType.PNG, oldImgName,
                Files.size(fileUuidPath), Timestamp.valueOf(LocalDateTime.now()), sheldonAdmin);

        when(fileRepoMock.save(any(File.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        FileLinkDTO fileLinkDTO = fileService.replace(newFile, oldFileEntity, sheldonAdmin);
        assertEquals(fileUuid, fileLinkDTO.getFileUuid());
        assertTrue(oldFileSize < Files.size(fileUuidPath));
        BufferedImage bufferedImage = ImageIO.read(fileUuidPath.toFile());
        int newImgHeight = bufferedImage.getHeight();
        assertTrue(oldImgHeight < newImgHeight);
        verify(fileRepoMock, times(1)).save(any(File.class));
    }

    @Test
    public void replaceJpgToLargeHeightClient() throws IOException {
        //prepare
        Long userId = 1L;
        User pennyClient = UserUtils.clientPennyTeller(userId);

        String oldImgName = "test.jpeg";
        Path pathTestJpeg = UPLOAD_PATH.resolve(oldImgName);

        final String fileUuid = "e9365637-bb5b-4792-9f25-ad36776fccf3";
        Path fileUuidPath = createFileUuidPath(fileUuid);
        Files.copy(pathTestJpeg, fileUuidPath, StandardCopyOption.REPLACE_EXISTING);
        Long oldFileSize = Files.size(fileUuidPath);
        File oldFileEntity = new File(UUID.fromString(fileUuid), FileType.JPEG, oldImgName,
                Files.size(fileUuidPath), Timestamp.valueOf(LocalDateTime.now()), pennyClient);

        MultipartFile newFile = getImgWithType("testLargeHeight", "jpeg");

        when(fileRepoMock.save(any(File.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        FileLinkDTO fileLinkDTO = fileService.replace(newFile, oldFileEntity, pennyClient);
        assertEquals(fileUuid, fileLinkDTO.getFileUuid());
        assertTrue(oldFileSize < Files.size(fileUuidPath));
        BufferedImage bufferedImage = ImageIO.read(fileUuidPath.toFile());
        int newImgHeight = bufferedImage.getHeight();
        assertEquals(CLIENT_IMAGE_HEIGHT, newImgHeight);
        verify(fileRepoMock, times(1)).save(any(File.class));
    }

    @Test
    public void replacePngClient() throws IOException {
        //prepare
        Long userId = 1L;
        User pennyClient = UserUtils.clientPennyTeller(userId);

        String oldImgName = "test.jpeg";
        int oldImgHeight = pictureNamesAndSize.get("test")[1];
        Path pathTestJpeg = UPLOAD_PATH.resolve(oldImgName);

        final String fileUuid = "e9365637-bb5b-4792-9f25-ad36776fccf3";
        Path fileUuidPath = createFileUuidPath(fileUuid);
        Files.copy(pathTestJpeg, fileUuidPath, StandardCopyOption.REPLACE_EXISTING);
        Long oldFileSize = Files.size(fileUuidPath);

        File oldFileEntity = new File(UUID.fromString(fileUuid), FileType.JPEG, oldImgName,
                Files.size(fileUuidPath), Timestamp.valueOf(LocalDateTime.now()), pennyClient);

        MultipartFile newFile = getImgWithType("test", "png");
        Long newFileSizeBeforeUpload = newFile.getSize();

        when(fileRepoMock.save(any(File.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        FileLinkDTO fileLinkDTO = fileService.replace(newFile, oldFileEntity, pennyClient);
        assertEquals(fileUuid, fileLinkDTO.getFileUuid());
        Long newFileSizeAfterUpload = Files.size(fileUuidPath);
        assertTrue(newFileSizeBeforeUpload < newFileSizeAfterUpload);
        BufferedImage bufferedImage = ImageIO.read(fileUuidPath.toFile());
        int newImgHeight = bufferedImage.getHeight();
        assertEquals(oldImgHeight, newImgHeight);
        verify(fileRepoMock, times(1)).save(any(File.class));
    }

    @Test
    public void replaceJpgToLargeWidthPngClient() throws IOException {
        //prepare
        Long userId = 1L;
        User pennyClient = UserUtils.clientPennyTeller(userId);

        String oldImgName = "test.jpeg";
        int oldImgHeight = pictureNamesAndSize.get("test")[1];
        Path pathTestJpeg = UPLOAD_PATH.resolve(oldImgName);

        final String fileUuid = "03b2552c-6981-4087-b5ee-5a5655fde4da";
        Path fileUuidPath = createFileUuidPath(fileUuid);
        Files.copy(pathTestJpeg, fileUuidPath, StandardCopyOption.REPLACE_EXISTING);
        Long oldFileSize = Files.size(fileUuidPath);

        File oldFileEntity = new File(UUID.fromString(fileUuid), FileType.JPEG, oldImgName,
                Files.size(fileUuidPath), Timestamp.valueOf(LocalDateTime.now()), pennyClient);

        MultipartFile newFile = getImgWithType("testLargeWidth", "png");
        Long newFileSizeBeforeUpload = newFile.getSize();

        when(fileRepoMock.save(any(File.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        FileLinkDTO fileLinkDTO = fileService.replace(newFile, oldFileEntity, pennyClient);
        assertEquals(fileUuid, fileLinkDTO.getFileUuid());
        Long newFileSizeAfterUpload = Files.size(fileUuidPath);
        assertTrue(newFileSizeBeforeUpload < newFileSizeAfterUpload);
        BufferedImage bufferedImage = ImageIO.read(fileUuidPath.toFile());
        int newImgWidth = bufferedImage.getWidth();
        assertEquals(CLIENT_IMAGE_WIDTH, newImgWidth);
        verify(fileRepoMock, times(1)).save(any(File.class));
    }

    @Test
    public void replaceJpgNotOwnerNotAdmin() throws IOException {
        //prepare
        Long userId = 1L;
        User pennyClient = UserUtils.clientPennyTeller(userId);
        Long notOwnerId = 2L;
        User rajeshNotOwner = UserUtils.clientRajeshKoothrappali(notOwnerId);

        String oldImgName = "test.jpeg";
        Path pathTestJpeg = UPLOAD_PATH.resolve(oldImgName);

        final String fileUuid = "e897e931-12ce-4b3f-afbc-42f6c165dfc8";
        Path fileUuidPath = createFileUuidPath(fileUuid);
        Files.copy(pathTestJpeg, fileUuidPath, StandardCopyOption.REPLACE_EXISTING);

        File oldFileEntity = new File(UUID.fromString(fileUuid), FileType.JPEG, oldImgName,
                Files.size(fileUuidPath), Timestamp.valueOf(LocalDateTime.now()), pennyClient);

        MultipartFile newFile = getImgWithType("testLargeWidth", "png");

        when(fileRepoMock.save(any(File.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        Exception exception = assertThrows(CustomAccessDeniedException.class, () -> {
            fileService.replace(newFile, oldFileEntity, rajeshNotOwner);
        });
        String perfectMessage = new CustomAccessDeniedException().getMessage();
        String resultMessage = exception.getMessage();
        assertEquals(perfectMessage, resultMessage);
        verify(fileRepoMock, never()).save(any(File.class));
    }

    private Path createFileUuidPath(String fileUuid) throws IOException {
        final String fileParentDir = fileUuid.substring(0, 2);
        Path parentDirPath = UPLOAD_PATH.resolve(fileParentDir);
        Files.createDirectories(parentDirPath);
        return parentDirPath.resolve(fileUuid);
    }

    @Test
    public void getFileList() {
        Long ownerId = 1L;
        User howardOwner = UserUtils.courierHowardWolowitz(ownerId);
        File file1 = new File(UUID.randomUUID(), FileType.JPEG, "test",
                0L, Timestamp.valueOf(LocalDateTime.now()), howardOwner);
        File file2 = new File(UUID.randomUUID(), FileType.JPEG, "test2",
                0L, Timestamp.valueOf(LocalDateTime.now()), howardOwner);
        List<File> fileList = new ArrayList<>();
        fileList.add(file1);
        fileList.add(file2);
        Pageable pageable = PageRequest.of(0, 2);
        Page page = new PageImpl(fileList, pageable, fileList.size());

        when(fileRepoMock.findAll(pageable)).thenReturn(page);

        List<FileInfoDTO> resultDTOs = fileService.getAllFiles(pageable);
        List<FileInfoDTO> perfectDTOs = new ArrayList<>();
        for (File file : fileList) {
            perfectDTOs.add(createFileDTO(file));
        }
        assertEquals(perfectDTOs.size(), resultDTOs.size());
        assertEquals(perfectDTOs, resultDTOs);
    }
    private FileInfoDTO createFileDTO(File file) {
        String fileLink = createFileLink(file.getId());
        return new FileInfoDTO(file.getId().toString(), file.getType().getMediaType(),
                file.getName(), file.getSize(),
                file.getUploadDate(), fileLink,
                createUserDTO(file.getOwner()));
    }

    private String createFileLink(UUID fileUuid) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/file/")
                .path(fileUuid.toString())
                .toUriString();
    }

    private UserInfoDTO createUserDTO(User user) {
        return new UserInfoDTO(user.getId(), user.getRole().name(),
                user.getFullName(), user.getEmail(),
                user.getLastSigninDate(), user.getAvatarId());
    }



}
