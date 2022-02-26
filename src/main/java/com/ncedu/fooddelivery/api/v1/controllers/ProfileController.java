package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.file.FileLinkDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.*;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.services.ClientService;
import com.ncedu.fooddelivery.api.v1.services.ModeratorService;
import com.ncedu.fooddelivery.api.v1.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class ProfileController {
    //TODO: add photo to profile
    //TODO: delete photo from profile

    @Autowired
    UserService userService;
    @Autowired
    ClientService clientService;
    @Autowired
    ModeratorService moderatorService;

    @GetMapping("/api/v1/profile")
    public UserInfoDTO getProfile(
            @AuthenticationPrincipal User authedUser) {
        String authedUserRole = authedUser.getRole().name();
        Long authedUserId = authedUser.getId();
        if (Role.isCLIENT(authedUserRole)) {
            return clientService.getClientDTOById(authedUserId);
        }
        if (Role.isMODERATOR(authedUserRole)) {
            return moderatorService.getModeratorDTOById(authedUserId);
        }

        //for admin
        return userService.getUserDTOById(authedUserId);
    }

    @PutMapping("/api/v1/profile")
    public ResponseEntity<?> changeUserInfo(
            @Valid @RequestBody UserChangeInfoDTO newUserInfo,
            @AuthenticationPrincipal User authedUser) {
        String authedUserRole = authedUser.getRole().name();
        Long authedUserId = authedUser.getId();
        boolean isModified = false;
        if (Role.isCLIENT(authedUserRole)) {
           isModified = clientService.changeClientInfo(authedUserId, newUserInfo);
           return createModifyResponse(isModified);
        }
        //for moderator and admin allow only fullNameChange
        String newFullName = newUserInfo.getFullName();
        isModified = userService.changeFullName(authedUserId, newFullName); //TODO: change id on user entity!

        return createModifyResponse(isModified);
    }

    @PatchMapping("/api/v1/profile/email")
    public ResponseEntity<?> changeUserEmail(
            @Valid @RequestBody EmailChangeDTO newEmailInfo,
            @AuthenticationPrincipal User authedUser) {
        boolean isModified = false;
        isModified = userService.changeEmail(authedUser, newEmailInfo);
        return createModifyResponse(isModified);
    }

    @PatchMapping("/api/v1/profile/password")
    public ResponseEntity<?> changeUserPassword(
            @Valid @RequestBody PasswordChangeDTO passwordChangeDTO,
            @AuthenticationPrincipal User authedUser) {
        boolean isModified = false;
        isModified = userService.changePassword(authedUser, passwordChangeDTO);
        return  createModifyResponse(isModified);
    }

    @PatchMapping("/api/v1/profile/avatar")
    public ResponseEntity<?> addAvatar(
            @Valid @RequestBody FileLinkDTO fileLinkDTO,
            @AuthenticationPrincipal User authedUser) {
        UserInfoDTO refreshedInfo = userService.addAvatar(authedUser, fileLinkDTO.getFileUuid());
        return new ResponseEntity<>(refreshedInfo, HttpStatus.OK);
    }

    private ResponseEntity<?> createModifyResponse(boolean isModified) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("isModified", isModified);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }
}
