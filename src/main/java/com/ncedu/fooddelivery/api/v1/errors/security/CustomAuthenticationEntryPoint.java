package com.ncedu.fooddelivery.api.v1.errors.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncedu.fooddelivery.api.v1.errors.wrappers.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String msg = "You are not authenticated";
    private static final String uuid = "670acb10-7498-4a9a-bafe-7c0c98747653";

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException e) throws IOException {
        log.error(e.getMessage(), e);
        ObjectMapper objectMapper = new ObjectMapper();
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        // create custom error wrapper for response
        ApiError apiError = new ApiError(httpStatus, msg, uuid);
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
