package com.ncedu.fooddelivery.api.v1.repos;

import com.ncedu.fooddelivery.api.v1.entities.Client;
import com.ncedu.fooddelivery.api.v1.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepo extends CrudRepository<Client, Long> {
    Client findByPhoneNumber(String phoneNumber);
}
