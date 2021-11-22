package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.ClientInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.ModeratorInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.UserInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Client;
import com.ncedu.fooddelivery.api.v1.entities.Moderator;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class UserController {
    //TODO: add errors and error wrappers
    //TODO: replace returned object in methods on ResponseEntity
    //TODO: add updating special fields with PATCH http verb
    //TODO: add updating all fields with PUT http verb

    @Autowired UserService userService;
    @Autowired ClientService clientService;
    @Autowired ModeratorService moderatorService;

    @GetMapping("/api/v1/user/{id}")
    @PreAuthorize("isAuthenticated()")
    public UserInfoDTO getUserById(
            @PathVariable Long id,
            @AuthenticationPrincipal User authedUser
    ) {

        UserInfoDTO userInfo = userService.getUserDTOById(id);
        if (userInfo == null) {
            // TODO: error when user id is not presented
            return null;
        }

        String authedUserRole = authedUser.getRole().name();
        //client can watch only own profile
        if (Role.isCLIENT(authedUserRole)) {
            Long authedId = authedUser.getId();
            if (userInfo.getId() == authedId) {
                ClientInfoDTO  clientProfile = clientService.getClientDTOById(authedId);
                return clientProfile;
            }
            // TODO: return 403 error (Forbidden) if requested not own profile
            return null;
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
