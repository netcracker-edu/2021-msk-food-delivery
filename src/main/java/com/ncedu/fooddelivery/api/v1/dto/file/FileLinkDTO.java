package com.ncedu.fooddelivery.api.v1.dto.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileLinkDTO {

    private String link;
    private String fileUuid;

    public FileLinkDTO() {};
}
