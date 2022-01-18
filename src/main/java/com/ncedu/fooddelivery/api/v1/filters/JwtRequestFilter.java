package com.ncedu.fooddelivery.api.v1.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncedu.fooddelivery.api.v1.configs.SecurityConfig;
import com.ncedu.fooddelivery.api.v1.errors.wrappers.ApiError;
import com.ncedu.fooddelivery.api.v1.services.impls.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
//TODO: refresh token

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
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

        final String header = request.getHeader(jwtTokenUtil.HEADER);

        if (jwtTokenUtil.isAuthHeaderNotValid(header)) {
            filterChain.doFilter(request, response);  		// If not valid, go to the next filter.
            return;
        }

        String jwtToken = jwtTokenUtil.getJwt(header);

        try {
            if (jwtTokenUtil.isTokenValid(jwtToken) && jwtTokenUtil.isTokenNotExpired(jwtToken)) {
                String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // After setting the Authentication in the context, we specify
                    // that the current user is authenticated. So it passes the
                    // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                filterChain.doFilter(request, response);
            }
        } catch (ExpiredJwtException e) {
            ApiError apiError = createJwtExpiredError(e);
            sendErrorResponse(apiError, response);
        } catch (SignatureException e) {
            ApiError apiError = createJwtSignatureError(e);
            sendErrorResponse(apiError, response);
        }
    }

    private ApiError createJwtExpiredError(Exception e) {
        final String uuid = "421de66a-1529-4124-b363-a92d85f0e8a2";
        final String msg = "JWT expired";
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        // create custom error wrapper for response
        ApiError apiError = new ApiError(httpStatus, msg, uuid);
        apiError.setDebugMessage(e.getMessage());
        return apiError;
    }

    private ApiError createJwtSignatureError(SignatureException e) {
        final String uuid = "8eb93a63-6fa3-4143-af39-74b64cdd7135";
        final String msg = "JWT not valid";
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        // create custom error wrapper for response
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
