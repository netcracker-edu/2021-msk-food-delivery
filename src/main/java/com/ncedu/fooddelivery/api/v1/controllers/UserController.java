package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.ClientInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.ModeratorInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.UserChangeInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.UserInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.notfound.UserNotFoundException;
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAccessDeniedException;
import com.ncedu.fooddelivery.api.v1.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class UserController {
    //TODO: replace returned object in methods on ResponseEntity
    //TODO: add updating special fields with PATCH http verb
    //TODO: add updating other fields with PUT http verb

    @Autowired UserService userService;
    @Autowired ClientService clientService;
    @Autowired ModeratorService moderatorService;

    @GetMapping("/api/v1/user/{id}")
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
    public UserInfoDTO getUserById(
            @PathVariable Long id,
            @AuthenticationPrincipal User authedUser
    ) {

        UserInfoDTO userInfo = userService.getUserDTOById(id);
        if (userInfo == null) {
            throw new UserNotFoundException("User not found id {" + id + "}");
        }

        //get extended info depending on the role of the requested user
        String userRole = userInfo.getRole();
        Long userId = userInfo.getId();
        if (Role.isCLIENT(userRole)) {
            ClientInfoDTO clientInfo = clientService.getClientDTOById(userId);
            return clientInfo;
        }
        if (Role.isMODERATOR(userRole)) {
            ModeratorInfoDTO moderatorInfo = moderatorService.getModeratorDTOById(userId);
            return moderatorInfo;
        }
        //if user role is ADMIN
        return userInfo;
    }

    @PutMapping("/api/v1/user/{id}")
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
    public ResponseEntity<?> changeUserInfo(
            @PathVariable Long id,
            @Valid @RequestBody UserChangeInfoDTO newUserInfo,
            @AuthenticationPrincipal User authedUser) {

        //client can change only own profile
        String authedUserRole = authedUser.getRole().name();
        if (Role.isCLIENT(authedUserRole)) {
            if (id.equals(authedUser.getId())) {
                boolean isModified = clientService.changeClientInfo(id, newUserInfo);
                return new ResponseEntity<>(isModified, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/user/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id
    ) {
        boolean isDeleted = userService.deleteUserById(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/api/v1/user/{id}/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> changeUserRole(
            @PathVariable Long id,
            @RequestBody Role role
    ) {
        System.out.println("Requested id: " + id);
        System.out.println("Requested role: " + role);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/v1/admins")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserInfoDTO> getAdminList() {
        List<UserInfoDTO> userList = userService.getAllAdmins();
        return userList;
    }

    @GetMapping("/api/v1/users")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public List<UserInfoDTO> getUserList() {
        List<UserInfoDTO> userList = userService.getAllUsers();
        return userList;
    }

}
