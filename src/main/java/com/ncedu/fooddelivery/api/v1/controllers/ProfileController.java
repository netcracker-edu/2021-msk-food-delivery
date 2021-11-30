package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.user.UserChangeInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.UserInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.services.ClientService;
import com.ncedu.fooddelivery.api.v1.services.ModeratorService;
import com.ncedu.fooddelivery.api.v1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ProfileController {

    @Autowired
    UserService userService;
    @Autowired
    ClientService clientService;
    @Autowired
    ModeratorService moderatorService;

    @GetMapping("/api/v1/profile")
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("isAuthenticated()")
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
        isModified = userService.changeFullName(authedUserId, newFullName);

        return createModifyResponse(isModified);
    }

    private ResponseEntity<?> createModifyResponse(boolean isModified) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("isModified", isModified);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }
}