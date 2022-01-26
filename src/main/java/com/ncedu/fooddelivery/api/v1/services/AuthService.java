package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.isCreatedDTO;
import com.ncedu.fooddelivery.api.v1.dto.jwt.JwtRequestDTO;
import com.ncedu.fooddelivery.api.v1.dto.jwt.JwtResponseDTO;
import com.ncedu.fooddelivery.api.v1.dto.jwt.RefreshTokenDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.NewUserDTO;

public interface AuthService {

    public isCreatedDTO signUp(NewUserDTO userInfo);
    public JwtResponseDTO signIn(JwtRequestDTO authInfo, String userAgent);
    public void signOut(RefreshTokenDTO refreshTokenDTO);
    public JwtResponseDTO refresh(RefreshTokenDTO refreshTokenDTO, String authHeader);

}
