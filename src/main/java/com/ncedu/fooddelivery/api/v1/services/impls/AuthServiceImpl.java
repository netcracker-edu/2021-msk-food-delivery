package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.isCreatedDTO;
import com.ncedu.fooddelivery.api.v1.dto.jwt.JwtRequestDTO;
import com.ncedu.fooddelivery.api.v1.dto.jwt.JwtResponseDTO;
import com.ncedu.fooddelivery.api.v1.dto.jwt.RefreshTokenDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.NewUserDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.UserInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.*;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.RefreshTokenException;
import com.ncedu.fooddelivery.api.v1.filters.JwtUtil;
import com.ncedu.fooddelivery.api.v1.mappers.RegistrationMapper;
import com.ncedu.fooddelivery.api.v1.repos.ClientRepo;
import com.ncedu.fooddelivery.api.v1.repos.ModeratorRepo;
import com.ncedu.fooddelivery.api.v1.repos.UserRepo;
import com.ncedu.fooddelivery.api.v1.services.AuthService;
import com.ncedu.fooddelivery.api.v1.services.UserRefreshTokenService;
import com.ncedu.fooddelivery.api.v1.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRefreshTokenService userRefreshTokenService;

    /*
        for signUp method
    */
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private ModeratorRepo moderatorRepo;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public isCreatedDTO signUp(NewUserDTO userInfo) {
        RegistrationMapper regMapper = RegistrationMapper.INSTANCE;
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        User user = regMapper.dtoToUser(userInfo);
        user.setRegDate(Timestamp.valueOf(LocalDateTime.now()));

        if (Role.isCLIENT(userInfo.getRole())) {
            Client client = regMapper.dtoToClient(userInfo);
            user.setClient(client);
        }
        if (Role.isMODERATOR(userInfo.getRole())) {
            Moderator moderator = regMapper.dtoToModerator(userInfo);
            user.setModerator(moderator);
        }
        user = userRepo.save(user);
        Long userId = user.getId();

        return new isCreatedDTO(userId);
    }

    @Override
    public JwtResponseDTO signIn(JwtRequestDTO authInfo, String userAgent) {
        Authentication auth = authenticate(authInfo.getLogin(), authInfo.getPassword());
        User user = (User) auth.getPrincipal(); //User implements UserDetails
        userService.setLastSigninFromNow(user);
        log.debug("USER: " + user.getUsername() + user.getLastSigninDate());
        final String accessToken = jwtUtil.createToken(user);
        final String refreshToken = userRefreshTokenService.createRefreshToken(user, userAgent);
        return new JwtResponseDTO(accessToken, refreshToken, createUserDTO(user));
    }

    private Authentication authenticate(String login, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password));
    }

    @Override
    public void signOut(RefreshTokenDTO refreshTokenDTO) {
        UUID refreshToken = UUID.fromString(refreshTokenDTO.getRefreshToken());
        UserRefreshToken urt = userRefreshTokenService.getTokenById(refreshToken);
        userService.setLastSigninFromNow(urt.getOwner());
        userRefreshTokenService.deleteToken(urt);
    }

    @Override
    public JwtResponseDTO refresh(RefreshTokenDTO refreshTokenDTO, String authHeader) {
        UUID refreshToken = UUID.fromString(refreshTokenDTO.getRefreshToken());
        UserRefreshToken urt = userRefreshTokenService.getTokenById(refreshToken);

        //change lastSignin on owner of token
        userService.setLastSigninFromNow(urt.getOwner());
        final String accessToken = jwtUtil.createToken(urt.getOwner());
        return new JwtResponseDTO(accessToken, refreshToken.toString(), createUserDTO(urt.getOwner()));
    }

    //check user name from expired JWT and from owner of refresh token
    private boolean checkUsernamesNotEquals(UserRefreshToken urt, String authHeader) {
        if (jwtUtil.isAuthHeaderNotValid(authHeader)) {
            return true;
        }
        String jwtToken = jwtUtil.getJwt(authHeader);
        String usernameFromToken = getUsernameFromToken(jwtToken);
        String usernameFromDB = urt.getOwner().getEmail();
        if (usernameFromDB.equals(usernameFromToken)) {
           return false;
        }
        return true;
    }

    private String getUsernameFromToken(String jwtToken) {
        try {
            return jwtUtil.getUsernameFromToken(jwtToken);
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            return claims.getSubject();
        }
    }

    private UserInfoDTO createUserDTO(User user) {
        return new UserInfoDTO(user.getId(), user.getRole().name(),
                user.getFullName(), user.getEmail(),
                user.getLastSigninDate(), user.getAvatarId());
    }
}
