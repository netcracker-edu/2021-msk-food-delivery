package com.ncedu.fooddelivery.api.v1.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SearchDTO {

    @NotBlank
    private String phrase;
}
