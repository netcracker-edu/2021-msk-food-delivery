package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.jwt.JwtRequestDTO;
import com.ncedu.fooddelivery.api.v1.dto.jwt.JwtResponseDTO;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.filters.JwtTokenUtil;
import com.ncedu.fooddelivery.api.v1.services.UserService;
import com.ncedu.fooddelivery.api.v1.services.impls.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

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

    @PostMapping("/api/v1/login")
    public ResponseEntity<?> login(@Valid @RequestBody JwtRequestDTO authInfo) {
        String login = authInfo.getLogin();
        Authentication auth = authenticate(login, authInfo.getPassword());
        //User implements UserDetails
        User user = (User) auth.getPrincipal();
        userService.setLastSigninFromNow(user);
        log.debug("USER: " + user.getUsername() + user.getLastSigninDate());
        final String token = jwtTokenUtil.createToken(user);
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    private Authentication authenticate(String login, String password) {
        return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login, password));
    }
}
