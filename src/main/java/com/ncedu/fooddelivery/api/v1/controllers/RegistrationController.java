package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.user.RegistrationDTO;
import com.ncedu.fooddelivery.api.v1.dto.isCreatedDTO;
import com.ncedu.fooddelivery.api.v1.services.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class RegistrationController {
    //TODO: add unit tests

    @Autowired
    RegistrationService registrationService;

    @PostMapping("/api/v1/registration")
    public isCreatedDTO registration(@Valid @RequestBody RegistrationDTO userInfo) {
        log.debug("POST /api/v1/registration");
        Long userId = registrationService.addUser(userInfo);
        isCreatedDTO isCreated = new isCreatedDTO();
        isCreated.setId(userId);
        log.debug("Created user: " + userId);
        return isCreated;
    }
}
