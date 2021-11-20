package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.entities.Client;
import com.ncedu.fooddelivery.api.v1.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {
    @Autowired
    ClientService clientService;

    @GetMapping("/api/v1/client/{id}")
    public Client getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);
        return  client;
    }
}
