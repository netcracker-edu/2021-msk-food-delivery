package com.ncedu.fooddelivery.api.v1.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncedu.fooddelivery.api.v1.configs.SecurityConfig;
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAuthenticationEntryPoint;
import com.ncedu.fooddelivery.api.v1.errors.wrappers.ApiError;
import com.ncedu.fooddelivery.api.v1.services.impls.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String pattern : SecurityConfig.permitAllPaths) {
            if (antPathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //check validity of Authorization header from request
        final String authHeader = request.getHeader(jwtUtil.HEADER);
        if (jwtUtil.isAuthHeaderNotValid(authHeader)) {
            filterChain.doFilter(request, response);  		// If not valid, go to the next filter.
            return;
        }

        String token = jwtUtil.getJwt(authHeader);
        try {
            if (jwtUtil.isTokenValid(token) && jwtUtil.isTokenNotExpired(token)) {
                UserDetails userDetails = getUserDetails(token);
                if (!userDetails.isAccountNonLocked()) {
                    sendLockError(request, response);
                    return;
                }
                UsernamePasswordAuthenticationToken upaToken  = createUpaToken(userDetails, request);
                SecurityContextHolder.getContext().setAuthentication(upaToken);
                filterChain.doFilter(request, response);
            }
        } catch (ExpiredJwtException e) {
            final String uuid = "421de66a-1529-4124-b363-a92d85f0e8a2";
            final String msg = "JWT expired";
            ApiError apiError = createApiError(uuid, msg, e);
            sendErrorResponse(apiError, response);
        } catch (SignatureException e) {
            final String uuid = "8eb93a63-6fa3-4143-af39-74b64cdd7135";
            final String msg = "JWT not valid";
            ApiError apiError = createApiError(uuid, msg, e);
            sendErrorResponse(apiError, response);
        }
    }

    private UserDetails getUserDetails(String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        return userDetailsService.loadUserByUsername(username);
    }

    private void sendLockError(HttpServletRequest request, HttpServletResponse response) {
        try {
            new CustomAuthenticationEntryPoint().commence(request, response, new LockedException("User account is locked"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private UsernamePasswordAuthenticationToken createUpaToken(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken upaToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        upaToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return upaToken;
    }

    private ApiError createApiError(String uuid, String msg, JwtException e) {
        log.error(e.getMessage(), e);
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ApiError apiError = new ApiError(httpStatus, msg, uuid);
        apiError.setDebugMessage(e.getMessage());
        return apiError;
    }

    private void sendErrorResponse(ApiError apiError, HttpServletResponse response) throws IOException {
        //for error objects processing
        ObjectMapper objectMapper = new ObjectMapper();
        // setting the response HTTP status and content type
        response.setStatus(apiError.getStatus().value());
        response.setContentType(APPLICATION_JSON_VALUE);
        // serializing the response body in JSON
        response
                .getOutputStream()
                .println(
                        objectMapper.writeValueAsString(apiError)
                );
    }
}
