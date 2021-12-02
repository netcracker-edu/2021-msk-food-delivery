package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.ClientInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Client;

public interface ClientService {

    public Client getClientById(Long id);
    public ClientInfoDTO getClientDTOById(Long id);
}
