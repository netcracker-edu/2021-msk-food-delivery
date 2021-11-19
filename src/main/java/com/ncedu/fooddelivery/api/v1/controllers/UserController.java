package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.UserCommonInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/api/v1/userFake/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT', 'MODERATOR')")
    public UserCommonInfoDTO getUserFakeById(@PathVariable Long id) {

        UserCommonInfoDTO userDTO = new UserCommonInfoDTO(
                "ADMIN",
                "АЛЕША",
                "admin@mail.ru",
                Timestamp.valueOf(LocalDateTime.now()),
                null);
          return userDTO;
    }

    @GetMapping("/api/v1/user/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        return user;
    }

}
