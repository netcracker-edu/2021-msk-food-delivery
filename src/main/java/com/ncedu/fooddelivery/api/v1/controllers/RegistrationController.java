package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.user.RegistrationDTO;
import com.ncedu.fooddelivery.api.v1.dto.IsCreatedDTO;
import com.ncedu.fooddelivery.api.v1.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;

    @PostMapping("/api/v1/registration")
    public IsCreatedDTO registration(@Valid @RequestBody RegistrationDTO userInfo) {

        Long userId = registrationService.addUser(userInfo);
        IsCreatedDTO isCreated = new IsCreatedDTO();
        isCreated.setId(userId);

        return isCreated;
    }
}
