package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.jwt.JwtRequestDTO;
import com.ncedu.fooddelivery.api.v1.dto.jwt.JwtResponseDTO;
import com.ncedu.fooddelivery.api.v1.dto.jwt.RefreshTokenDTO;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.entities.UserRefreshToken;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.RefreshTokenException;
import com.ncedu.fooddelivery.api.v1.filters.JwtTokenUtil;
import com.ncedu.fooddelivery.api.v1.services.UserRefreshTokenService;
import com.ncedu.fooddelivery.api.v1.services.UserService;
import com.ncedu.fooddelivery.api.v1.services.impls.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@Slf4j
public class AuthController {
//TODO: logout controller
//TODO: refresh token controller

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRefreshTokenService userRefreshTokenService;

    @PostMapping("/api/v1/auth/signin")
    public ResponseEntity<?> signin(
            @Valid @RequestBody JwtRequestDTO authInfo,
            @RequestHeader(value = HttpHeaders.USER_AGENT) String userAgent) {
        String login = authInfo.getLogin();
        Authentication auth = authenticate(login, authInfo.getPassword());
        //User implements UserDetails
        User user = (User) auth.getPrincipal();
        userService.setLastSigninFromNow(user);
        log.debug("USER: " + user.getUsername() + user.getLastSigninDate());
        final String accessToken = jwtTokenUtil.createToken(user);
        String refreshToken = userRefreshTokenService.createRefreshToken(user, userAgent);
        return ResponseEntity.ok(new JwtResponseDTO(accessToken, refreshToken));
    }

    private Authentication authenticate(String login, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password));
    }

    @PostMapping("/api/v1/auth/signout")
    public ResponseEntity<?> signout(
            @Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        UUID refreshToken = UUID.fromString(refreshTokenDTO.getRefreshToken());
        userRefreshTokenService.deleteTokenById(refreshToken);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/auth/refresh")
    public ResponseEntity<?> refresh(
            @Valid @RequestBody RefreshTokenDTO refreshTokenDTO,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader) {
        UUID refreshToken = UUID.fromString(refreshTokenDTO.getRefreshToken());
        UserRefreshToken urt = userRefreshTokenService.getTokenById(refreshToken);
        if (jwtTokenUtil.isAuthHeaderNotValid(authHeader)) {
            throw new RefreshTokenException();
        }
        /*check user name from expired JWT and from owner of refresh token*/
        String jwtToken = jwtTokenUtil.getJwt(authHeader);
        String usernameFromToken = getUsernameFromToken(jwtToken);
        String usernameFromDB = urt.getOwner().getEmail();
        if (!usernameFromDB.equals(usernameFromToken)) {
            throw new RefreshTokenException();
        }

        final String accessToken = jwtTokenUtil.createToken(urt.getOwner());
        return ResponseEntity.ok(new JwtResponseDTO(accessToken, refreshToken.toString()));
    }

    private String getUsernameFromToken(String jwtToken) {
        try {
            return jwtTokenUtil.getUsernameFromToken(jwtToken);
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            return claims.getSubject();
        }
    }

}
