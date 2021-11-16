package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.entities.Moderator;
import com.ncedu.fooddelivery.api.v1.services.ModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModeratorController {
    @Autowired
    ModeratorService moderatorService;

    @GetMapping("/api/v1/moderator/{id}")
    public Moderator getModeratorById(@PathVariable Long id) {
        Moderator moderator = moderatorService.getModeratorById(id);
        return moderator;
    }
}
