package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.entities.Moderator;
import com.ncedu.fooddelivery.api.v1.repos.ModeratorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ModeratorService {
    @Autowired
    ModeratorRepo moderatorRepo;

    public Moderator getModeratorById(Long id) {
        Optional<Moderator> moderator = moderatorRepo.findById(id);
        if (moderator.isPresent()) {
            return moderator.get();
        }
        return null;
    }
}
