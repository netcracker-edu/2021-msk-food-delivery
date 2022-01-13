package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.jwt.JwtRequestDTO;
import com.ncedu.fooddelivery.api.v1.dto.jwt.JwtResponseDTO;
import com.ncedu.fooddelivery.api.v1.filters.JwtTokenUtil;
import com.ncedu.fooddelivery.api.v1.services.impls.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/api/v1/login")
    public ResponseEntity<?> login(@Valid @RequestBody JwtRequestDTO authInfo) {
        String login = authInfo.getLogin();
        authenticate(login, authInfo.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        final String token = jwtTokenUtil.createToken(userDetails);
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    //TODO: create special exception for GlobalExceptionHandler
    private void authenticate(String login, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login, password));
        } catch (DisabledException e) {
            throw new RuntimeException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("INVALID_CREDENTIALS", e);
        }
    }
}
