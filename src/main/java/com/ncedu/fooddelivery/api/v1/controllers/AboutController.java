package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.AboutDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AboutController {

    @GetMapping("/api/v1/whoami")
    public AboutDTO about() {
        AboutDTO aboutDTO = new AboutDTO();
        return aboutDTO;
    }
}
