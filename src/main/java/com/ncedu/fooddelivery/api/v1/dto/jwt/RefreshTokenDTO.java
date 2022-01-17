package com.ncedu.fooddelivery.api.v1.dto.jwt;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RefreshTokenDTO {
    @NotBlank
    private String refreshToken;
}
