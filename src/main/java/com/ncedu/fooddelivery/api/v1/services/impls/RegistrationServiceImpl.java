package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.user.RegistrationDTO;
import com.ncedu.fooddelivery.api.v1.entities.Client;
import com.ncedu.fooddelivery.api.v1.entities.Moderator;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.mappers.RegistrationMapper;
import com.ncedu.fooddelivery.api.v1.repos.ClientRepo;
import com.ncedu.fooddelivery.api.v1.repos.ModeratorRepo;
import com.ncedu.fooddelivery.api.v1.repos.UserRepo;
import com.ncedu.fooddelivery.api.v1.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired UserRepo userRepo;
    @Autowired ClientRepo clientRepo;
    @Autowired ModeratorRepo moderatorRepo;
    @Autowired PasswordEncoder encoder;

    public Long addUser(RegistrationDTO userInfo) {
        RegistrationMapper regMapper = RegistrationMapper.INSTANCE;
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        User user = regMapper.dtoToUser(userInfo);
        user.setRegDate(Timestamp.valueOf(LocalDateTime.now()));

        Long userId = -1L;

        if (Role.isCLIENT(userInfo.getRole())) {
            Client client = regMapper.dtoToClient(userInfo);
            client.setUser(user);
            client = clientRepo.save(client);
            userId = client.getId();
        } else if (Role.isMODERATOR(userInfo.getRole())) {
            Moderator moderator = regMapper.dtoToModerator(userInfo);
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