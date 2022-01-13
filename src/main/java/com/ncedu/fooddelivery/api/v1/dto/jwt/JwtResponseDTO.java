package com.ncedu.fooddelivery.api.v1.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponseDTO {

    private String token;
    public JwtResponseDTO() {};
}
