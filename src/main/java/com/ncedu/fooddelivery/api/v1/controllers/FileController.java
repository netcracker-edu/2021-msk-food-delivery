package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.entities.File;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAccessDeniedException;
import com.ncedu.fooddelivery.api.v1.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/api/v1/file")
    public Map<String,String> uploadFile (
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User authedUser) {
        log.info("POST /api/v1/file");
        String fileUUID = fileService.save(file, authedUser);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/file/")
                .path(fileUUID)
                .toUriString();
        log.info("Created file link: " + fileDownloadUri);
        Map<String, String> response = new HashMap<>();
        response.put("file", fileDownloadUri);
        return response;
    }
    @GetMapping("/api/v1/file/{file}")
    public ResponseEntity<Resource> download(
            @PathVariable File file,
            @AuthenticationPrincipal User authedUser) {
        log.info("GET /api/v1/file/" + file.getId().toString());
        Resource resource = fileService.load(file);
        String mediaType = file.getType().getMediaType();
        String fileNameWithExt = file.getName() + "." + file.getType().name();
        log.info("Sending file "+fileNameWithExt+"("+file.getId().toString() +")"+" to client");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mediaType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileNameWithExt +"\"")
                .body(resource);
    }

    @DeleteMapping("/api/v1/file/{file}")
    public ResponseEntity<?> delete(
            @PathVariable File file,
            @AuthenticationPrincipal User authedUser) {
        log.info("DELETE /api/v1/file/" + file.getId().toString());

        Long fileOwnerId = file.getOwner().getId();
        boolean isNotAdmin = !Role.isADMIN(authedUser.getRole());
        boolean isNotOwner = !fileOwnerId.equals(authedUser.getId());
        if (isNotAdmin && isNotOwner) {
            log.error(authedUser.getEmail() + " not Admin and not Owner of the file " + file.getId().toString());
            throw new CustomAccessDeniedException();
        }
        fileService.delete(file);
        log.info("File deleted: " + file.getId().toString());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
