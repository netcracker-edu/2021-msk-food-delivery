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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class RegistrationService {

    @Autowired UserRepo userRepo;
    @Autowired ClientRepo clientRepo;
    @Autowired ModeratorRepo moderatorRepo;
    @Autowired PasswordEncoder encoder;

    public Long addUser(RegistrationDTO userInfo) {
        //TODO: move set hell to "Map Struct"
        User user = new User();
        user.setEmail(userInfo.getEmail());
        user.setFullName(userInfo.getFullName());
        user.setPassword(encoder.encode(userInfo.getPassword()));
        Role role = Role.valueOf(userInfo.getRole());
        user.setRole(role);
        user.setAvatarId(user.getAvatarId());
        user.setRegDate(Timestamp.valueOf(LocalDateTime.now()));

        Long userId = -1L;

        if ("CLIENT".equals(role.name())) {
            Client client = new Client();
            client.setPaymentData(userInfo.getPaymentData());
            client.setRating(userInfo.getRating());
            client.setPhoneNumber(userInfo.getPhoneNumber());
            client.setUser(user);
            client = clientRepo.save(client);
            userId = client.getId();
        } else if ("MODERATOR".equals(role.name())) {
            Moderator moderator = new Moderator();
            moderator.setWarehouseId(userInfo.getWarehouseId());
            moderator.setUser(user);
            moderator = moderatorRepo.save(moderator);
            userId = moderator.getId();
        } else {
            user = userRepo.save(user);
            userId = user.getId();
        }

        return userId;
    }



}
