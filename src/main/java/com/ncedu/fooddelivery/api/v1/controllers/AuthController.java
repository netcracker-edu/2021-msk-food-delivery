package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.isCreatedDTO;
import com.ncedu.fooddelivery.api.v1.dto.jwt.JwtRequestDTO;
import com.ncedu.fooddelivery.api.v1.dto.jwt.JwtResponseDTO;
import com.ncedu.fooddelivery.api.v1.dto.jwt.RefreshTokenDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.NewUserDTO;
import com.ncedu.fooddelivery.api.v1.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@Slf4j
@RestController
public class AuthController {
    //TODO: add unit tests

    @Autowired
    private AuthService authService;

    @PostMapping("/api/v1/auth/signup")
    public isCreatedDTO signUp(@Valid @RequestBody NewUserDTO userInfo) {
        log.debug("POST /api/v1/auth/signup");
        return authService.signUp(userInfo);
    }

    @PostMapping("/api/v1/auth/signin")
    public ResponseEntity<?> signIn(
            @Valid @RequestBody JwtRequestDTO authInfo,
            @RequestHeader(value = HttpHeaders.USER_AGENT) String userAgent) {
        JwtResponseDTO jwtResponseDTO = authService.signIn(authInfo, userAgent);
        return ResponseEntity.ok(jwtResponseDTO);
    }

    @PostMapping("/api/v1/auth/signout")
    public ResponseEntity<?> signOut(
            @Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        authService.signOut(refreshTokenDTO);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/auth/refresh")
    public ResponseEntity<?> refresh(
            @Valid @RequestBody RefreshTokenDTO refreshTokenDTO,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader) {
        JwtResponseDTO jwtResponseDTO = authService.refresh(refreshTokenDTO, authHeader);
        return ResponseEntity.ok(jwtResponseDTO);
    }
}
