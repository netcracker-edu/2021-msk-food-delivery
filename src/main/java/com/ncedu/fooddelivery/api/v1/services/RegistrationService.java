package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.user.RegistrationDTO;

public interface RegistrationService {
    public Long addUser(RegistrationDTO userInfo);
}
