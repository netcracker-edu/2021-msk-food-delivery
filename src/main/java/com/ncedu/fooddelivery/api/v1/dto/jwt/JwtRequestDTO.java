package com.ncedu.fooddelivery.api.v1.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class JwtRequestDTO {

    @NotBlank
    @Size(min=11)
    private String login;
    @NotBlank
    @Size(min=8, max=64)
    private String password;

    public JwtRequestDTO() {}
}
