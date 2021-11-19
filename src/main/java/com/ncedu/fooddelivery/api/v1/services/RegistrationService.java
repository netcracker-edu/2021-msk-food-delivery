package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.RegistrationDTO;
import com.ncedu.fooddelivery.api.v1.entities.Client;
import com.ncedu.fooddelivery.api.v1.entities.Moderator;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.repos.ClientRepo;
import com.ncedu.fooddelivery.api.v1.repos.ModeratorRepo;
import com.ncedu.fooddelivery.api.v1.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class RegistrationService {

    @Autowired UserRepo userRepo;
    @Autowired ClientRepo clientRepo;
    @Autowired ModeratorRepo moderatorRepo;

    public Long addUser(RegistrationDTO userInfo) {
        //TODO: move set hell to "Map Struct"
        User user = new User();
        user.setEmail(userInfo.getEmail());
        user.setFullName(userInfo.getFullName());
        user.setPassword(userInfo.getPassword());
        Role role = Role.valueOf(userInfo.getRole());
        user.setRole(role);
        user.setAvatarId(user.getAvatarId());
        user.setRegDate(Timestamp.valueOf(LocalDateTime.now()));
        user = userRepo.save(user);

        //TODO: validating on unique email and phoneNumber(for clients and couriers)
        Long userId = user.getId();
        if (userId == null) {
            userId = -1L;
            return userId;
        }
        String userRole = user.getRole().name();

        if ("CLIENT".equals(userRole)) {
            Client client = new Client();
            client.setUser(user);
            //TODO: problems with insert JSON type
            client.setPaymentData(userInfo.getPaymentData());
            client.setRating(userInfo.getRating());
            client.setPhoneNumber(userInfo.getPhoneNumber());
            clientRepo.save(client);
        } else if ("MODERATOR".equals(userRole)) {
            Moderator moderator = new Moderator();
            moderator.setId(userId);
            moderator.setWarehouseId(userInfo.getWarehouseId());
            moderatorRepo.save(moderator);
        }

        return userId;
    }



}
