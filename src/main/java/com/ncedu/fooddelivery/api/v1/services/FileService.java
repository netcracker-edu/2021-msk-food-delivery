package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.file.FileInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.file.FileLinkDTO;
import com.ncedu.fooddelivery.api.v1.entities.File;
import com.ncedu.fooddelivery.api.v1.entities.User;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    public FileLinkDTO save(MultipartFile file, User owner);
    public Resource load(File file);
    public void delete(File file, User authedUser);
    public FileLinkDTO replace(MultipartFile newFile, File oldFile, User authedUser);
    public List<FileInfoDTO> getAllFiles(Pageable pageable);
}
