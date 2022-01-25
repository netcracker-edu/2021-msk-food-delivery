package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.file.FileInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.file.FileLinkDTO;
import com.ncedu.fooddelivery.api.v1.entities.File;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAccessDeniedException;
import com.ncedu.fooddelivery.api.v1.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
public class FileController {
    //TODO: add checks for client, courier uploaded photos (only jpeg + max size)
    //TODO: add relations with other entities
    //TODO: add trigger which deleting files without relations
    //TODO: add unit tests

    @Autowired
    FileService fileService;

    @PostMapping("/api/v1/file")
    public FileLinkDTO uploadFile(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User authedUser) {
        log.debug("POST /api/v1/file");
        FileLinkDTO fileLinkDTO = fileService.save(file, authedUser);
        log.debug("Created file link: " + fileLinkDTO.getLink());
        return fileLinkDTO;
    }

    @GetMapping("/api/v1/file/{file}")
    public ResponseEntity<?> download(
            @PathVariable File file,
            @AuthenticationPrincipal User authedUser) {
        log.debug("GET /api/v1/file/" + file.getId().toString());
        return buildDownloadResponseEntity(file);
    }

    private ResponseEntity<?> buildDownloadResponseEntity(File file) {
        Resource resource = fileService.load(file);
        String mediaType = file.getType().getMediaType();
        String fileNameWithExt = file.getName() + "." + file.getType().name();
        log.debug("Sending file "+fileNameWithExt+"("+file.getId().toString() +")"+" to client");
        return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mediaType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileNameWithExt +"\"")
                    .body(resource);
    }

    @PutMapping("/api/v1/file/{file}")
    public FileLinkDTO replace(
            @PathVariable File file,
            @RequestParam("newFile") MultipartFile newFile,
            @AuthenticationPrincipal User authedUser) {
        FileLinkDTO fileLinkDTO = fileService.replace(newFile, file, authedUser);
        return fileLinkDTO;
    }

    @DeleteMapping("/api/v1/file/{file}")
    public ResponseEntity<?> delete(
            @PathVariable File file,
            @AuthenticationPrincipal User authedUser) {
        log.debug("DELETE /api/v1/file/" + file.getId().toString());
        fileService.delete(file, authedUser);
        log.debug("File deleted: " + file.getId().toString());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/api/v1/files")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<FileInfoDTO> getAllFiles(
            @PageableDefault(sort = {"uploadDate"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        log.debug("GET /api/v1/files PAGE=" + pageable.getPageSize() + " SIZE=" +pageable.getPageSize());
        return fileService.getAllFiles(pageable);
    }

}
