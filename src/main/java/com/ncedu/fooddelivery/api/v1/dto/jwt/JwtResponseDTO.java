package com.ncedu.fooddelivery.api.v1.dto.jwt;

import com.ncedu.fooddelivery.api.v1.dto.user.UserInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponseDTO {

    private String accessToken;
    private String refreshToken;
    private UserInfoDTO user;
    public JwtResponseDTO() {};
}
