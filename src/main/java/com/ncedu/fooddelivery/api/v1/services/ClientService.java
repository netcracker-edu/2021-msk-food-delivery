package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.entities.Client;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.repos.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    ClientRepo clientRepo;

    public Client findClientById(Long id) {
        Optional<Client> findedClient = clientRepo.findById(id);
        if (findedClient.isPresent()) {
            return findedClient.get();
        }
        return null;
    }

}
