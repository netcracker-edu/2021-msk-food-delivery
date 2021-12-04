package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.user.*;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.PasswordsMismatchException;
import com.ncedu.fooddelivery.api.v1.services.ClientService;
import com.ncedu.fooddelivery.api.v1.services.ModeratorService;
import com.ncedu.fooddelivery.api.v1.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProfileControllerTest {

    @Mock
    UserService userServiceMock;
    @Mock
    ClientService clientServiceMock;
    @Mock
    ModeratorService moderatorServiceMock;

    @InjectMocks
    ProfileController profileController;

    @Test
    public void getAdminProfile() {
        UserInfoDTO userInfoDTO = new UserInfoDTO(1L, "ADMIN", "admin@mail.ru");
        when(userServiceMock.getUserDTOById(1L)).thenReturn(userInfoDTO);

        User user = new User();
        user.setId(1L);
        user.setRole(Role.ADMIN);

        UserInfoDTO resultUserDTO = profileController.getProfile(user);
        assertEquals(userInfoDTO, resultUserDTO);
    }

    @Test
    public void getClientProfile() {
        ClientInfoDTO clientInfoDTO = new ClientInfoDTO(1L,"CLIENT", "client@mail.ru");
        when(clientServiceMock.getClientDTOById(1L)).thenReturn(clientInfoDTO);

        User user = new User();
        user.setId(1L);
        user.setRole(Role.CLIENT);
        UserInfoDTO resultClientDTO = profileController.getProfile(user);
        assertEquals(clientInfoDTO, resultClientDTO);
    }

    @Test
    public void getModeratorProfile() {
        ModeratorInfoDTO moderatorInfoDTO = new ModeratorInfoDTO(1L, "MODERATOR", "moderator@mail.ru");
        when(moderatorServiceMock.getModeratorDTOById(1L)).thenReturn(moderatorInfoDTO);

        User user = new User();
        user.setId(1L);
        user.setRole(Role.MODERATOR);
        UserInfoDTO resultModeratorDTO = profileController.getProfile(user);
        assertEquals(moderatorInfoDTO, resultModeratorDTO);
    }

    @Test
    public void changeClientName() {
        UserChangeInfoDTO userChangeInfoDTO = new UserChangeInfoDTO();
        userChangeInfoDTO.setFullName("Sheldon Lee Cooper");
        Long authedUserId = 1L;
        when(clientServiceMock.changeClientInfo(authedUserId, userChangeInfoDTO)).thenReturn(true);

        User user = new User();
        user.setId(authedUserId);
        user.setRole(Role.CLIENT);
        ResponseEntity<?> resultResponse = profileController.changeUserInfo(userChangeInfoDTO, user);

        ResponseEntity<?> perfectResponse = createModifyResponse(true);
        assertEquals(perfectResponse, resultResponse);
    }

    @Test
    public void changeAdminName() {
        UserChangeInfoDTO userChangeInfoDTO = new UserChangeInfoDTO();
        userChangeInfoDTO.setFullName("Howard Joel Wolowitz");
        Long authedUserId = 1L;
        when(userServiceMock.changeFullName(authedUserId, userChangeInfoDTO.getFullName())).thenReturn(true);

        User user = new User();
        user.setId(authedUserId);
        user.setRole(Role.ADMIN);
        ResponseEntity<?> resultResponse = profileController.changeUserInfo(userChangeInfoDTO, user);

        ResponseEntity<?> perfectResponse = createModifyResponse(true);
        assertEquals(perfectResponse, resultResponse);
    }

    @Test
    public void changeAdminNameWithNullName() {
        //in DTO fullName not presented
        UserChangeInfoDTO userChangeInfoDTO = new UserChangeInfoDTO();
        userChangeInfoDTO.setPhoneNumber("+7 (800) 555 35-35");
        Long authedUserId = 1L;
        when(userServiceMock.changeFullName(authedUserId, userChangeInfoDTO.getFullName())).thenReturn(false);

        User user = new User();
        user.setId(authedUserId);
        user.setRole(Role.ADMIN);
        ResponseEntity<?> resultResponse = profileController.changeUserInfo(userChangeInfoDTO, user);

        ResponseEntity<?> perfectResponse = createModifyResponse(false);
        assertEquals(perfectResponse, resultResponse);
    }

    @Test
    public void changeEmail() {
        EmailChangeDTO emailChangeDTO = new EmailChangeDTO();
        emailChangeDTO.setEmail("wolowitz@bigbang.theory");
        emailChangeDTO.setPassword("bigBangTheory");
        User user = new User();
        user.setId(1L);
        when(userServiceMock.changeEmail(user, emailChangeDTO)).thenReturn(true);

        ResponseEntity<?> resultResponse = profileController.changeUserEmail(emailChangeDTO, user);

        ResponseEntity<?> perfectResponse = createModifyResponse(true);
        assertEquals(perfectResponse, resultResponse);
    }

    @Test
    public void changeEmailWithBadValues() {
        EmailChangeDTO emailChangeDTO = new EmailChangeDTO();
        emailChangeDTO.setEmail("wolowitz.bigbang");
        emailChangeDTO.setPassword("small");
        User user = new User();
        user.setId(1L);
        when(userServiceMock.changeEmail(user, emailChangeDTO)).thenReturn(false);

        ResponseEntity<?> resultResponse = profileController.changeUserEmail(emailChangeDTO, user);

        ResponseEntity<?> perfectResponse = createModifyResponse(false);
        assertEquals(perfectResponse, resultResponse);
    }

    @Test
    public void changePasswordSuccess() {
        PasswordChangeDTO pwdChangeDTO = new PasswordChangeDTO();
        pwdChangeDTO.setOldPassword("password");
        pwdChangeDTO.setNewPassword("qwerty123");
        pwdChangeDTO.setNewPasswordConfirm("qwerty123");
        User user = new User();
        user.setId(1L);
        when(userServiceMock.changePassword(user, pwdChangeDTO)).thenReturn(true);

        ResponseEntity<?> resultResponse =
                profileController.changeUserPassword(pwdChangeDTO, user);

        ResponseEntity<?> perfectResponse = createModifyResponse(true);

        verify(userServiceMock, times(1))
                .changePassword(user, pwdChangeDTO);
        assertEquals(perfectResponse, resultResponse);
    }

    @Test
    public void changePasswordMismatchError() {
        PasswordChangeDTO pwdChangeDTO = new PasswordChangeDTO();
        pwdChangeDTO.setOldPassword("password");
        pwdChangeDTO.setNewPassword("qwerty123");
        pwdChangeDTO.setNewPasswordConfirm("INCORRECT");
        User user = new User();
        user.setId(1L);
        when(userServiceMock.changePassword(user, pwdChangeDTO))
                .thenThrow(new PasswordsMismatchException());

        Exception exception = assertThrows(PasswordsMismatchException.class, () -> {
            profileController.changeUserPassword(pwdChangeDTO, user);
        });
        String resultMessage = exception.getMessage();
        String perfectMessage = new PasswordsMismatchException().getMessage();

        verify(userServiceMock, times(1))
                .changePassword(user, pwdChangeDTO);
        assertEquals(perfectMessage, resultMessage);
    }

    private ResponseEntity<?> createModifyResponse(boolean isModified) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("isModified", isModified);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }
}
