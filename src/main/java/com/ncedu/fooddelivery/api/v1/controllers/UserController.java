package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.UserDTO;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/api/v1/userFake/{id}")
    public UserDTO getUserFakeById(@PathVariable Long id) {

        UserDTO userDTO = new UserDTO(
                id,
                "ADMIN",
                "PASSWORD",
                "АЛЕША",
                "admin@mail.ru",
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now()),
                null,
                null);
        return userDTO;
    }

    @GetMapping("/api/v1/user/{id}")
    public User getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        if (user == null) {
            return null;
        }

        return user;
    }
}
