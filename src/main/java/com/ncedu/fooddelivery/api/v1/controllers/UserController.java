package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.ClientInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.ModeratorInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.UserInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Client;
import com.ncedu.fooddelivery.api.v1.entities.Moderator;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.services.ClientService;
import com.ncedu.fooddelivery.api.v1.services.ModeratorService;
import com.ncedu.fooddelivery.api.v1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class UserController {

    @Autowired UserService userService;
    @Autowired ClientService clientService;
    @Autowired ModeratorService moderatorService;

    @GetMapping("/api/v1/userFake/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT', 'MODERATOR')")
    public UserInfoDTO getUserFakeById(@PathVariable Long id) {

        UserInfoDTO userDTO = new UserInfoDTO(
                id,
                "ADMIN",
                "АЛЕША",
                "admin@mail.ru",
                Timestamp.valueOf(LocalDateTime.now()),
                null);
          return userDTO;
    }

    //TODO: check role for current user and add logic for other roles
    @GetMapping("/api/v1/user/{id}")
    @PreAuthorize("isAuthenticated()")
    public UserInfoDTO getUserById(@PathVariable Long id) {
        //TODO: error when user id is not presented
        UserInfoDTO userInfo = userService.getUserDTOById(id);
        if (userInfo == null) {
            return null;
        }
        String userRole = userInfo.getRole();
        Long userId = userInfo.getId();

        if ("CLIENT".equals(userRole)) {
            ClientInfoDTO clientInfo = clientService.getClientDTOById(userId);
            return clientInfo;
        }
        if ("MODERATOR".equals(userRole)) {
            ModeratorInfoDTO moderatorInfo = moderatorService.getModeratorDTOById(userId);
            return moderatorInfo;
        }

        return userInfo;
    }

    @GetMapping("/api/v1/admins")
    @PreAuthorize("isAuthenticated()")
    public List<UserInfoDTO> getAdminList() {
        List<UserInfoDTO> userList = userService.getAllAdmins();
        return userList;
    }

    @GetMapping("/api/v1/users")
    @PreAuthorize("isAuthenticated()")
    public List<UserInfoDTO> getUserList() {
        List<UserInfoDTO> userList = userService.getAllUsers();
        return userList;
    }

}
