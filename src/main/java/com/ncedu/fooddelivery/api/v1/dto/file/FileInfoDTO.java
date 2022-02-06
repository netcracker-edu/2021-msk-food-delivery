package com.ncedu.fooddelivery.api.v1.dto.file;

import com.ncedu.fooddelivery.api.v1.dto.user.UserInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class FileInfoDTO {

    private String id;
    private String type;
    private String name;
    private Long size;
    private Timestamp uploadDate;
    private String link;
    private UserInfoDTO owner;

    public FileInfoDTO() {};
}
