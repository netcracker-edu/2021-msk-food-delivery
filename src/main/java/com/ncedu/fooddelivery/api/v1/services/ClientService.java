package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.user.ClientInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.UserChangeInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Client;

public interface ClientService {

    public boolean changeClientInfo(Long id, UserChangeInfoDTO newUserInfo);

    public Client getClientById(Long id);
    public ClientInfoDTO getClientDTOById(Long id);
}
