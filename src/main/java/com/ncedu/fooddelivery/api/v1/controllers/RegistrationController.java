package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.RegistrationDTO;
import com.ncedu.fooddelivery.api.v1.dto.isCreatedDTO;
import com.ncedu.fooddelivery.api.v1.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;

    @PostMapping("/api/v1/registration")
    public isCreatedDTO registration(@RequestBody RegistrationDTO userInfo) {

        Long userId = registrationService.addUser(userInfo);
        isCreatedDTO isCreated = new isCreatedDTO();
        isCreated.setId(userId);

        return isCreated;
    }
}
