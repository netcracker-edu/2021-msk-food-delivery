package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public User findUserById(Long id) {
        Optional<User> findedUser = userRepo.findById(id);
        if (findedUser.isPresent()) {
            return findedUser.get();
        }
        return null;
    }
}
