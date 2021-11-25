package com.ncedu.fooddelivery.api.v1.errors.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncedu.fooddelivery.api.v1.errors.wrappers.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String message = "You are not authenticated";
    private static final String uuid = "670acb10-7498-4a9a-bafe-7c0c98747653";

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        // create custom error wrapper for response
        ApiError apiError = new ApiError(httpStatus, message, uuid);
        // setting the response HTTP status and content type
        response.setStatus(httpStatus.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        // serializing the response body in JSON
        response
                .getOutputStream()
                .println(
                        objectMapper.writeValueAsString(apiError)
                );
    }
}