package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.deliverySession.DeliverySessionInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.isCreatedDTO;
import com.ncedu.fooddelivery.api.v1.entities.Courier;
import com.ncedu.fooddelivery.api.v1.entities.DeliverySession;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.services.DeliverySessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DeliverySessionController {
    @Autowired
    DeliverySessionService deliverySessionService;

    @PreAuthorize("hasAnyAuthority('COURIER', 'MODERATOR', 'ADMIN')")
    @GetMapping("/api/v1/deliverySession/{deliverySession}")
    public ResponseEntity<DeliverySessionInfoDTO> getById(@AuthenticationPrincipal User user,
                                          @PathVariable DeliverySession deliverySession){
        return new ResponseEntity<>(deliverySessionService.getInfo(user, deliverySession), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COURIER')")
    @PostMapping("/api/v1/deliverySession")
    public ResponseEntity<isCreatedDTO> startSession(@AuthenticationPrincipal User user){
        return new ResponseEntity<>(deliverySessionService.startSession(user), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COURIER')")
    @PatchMapping("/api/v1/deliverySession")
    public ResponseEntity<Object> finishSession(@AuthenticationPrincipal User user){
        deliverySessionService.finishSession(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
    @PatchMapping("/api/v1/courier/{courier}/deliverySession")
    public ResponseEntity<Object> finishSession(@AuthenticationPrincipal User user,
                                                @PathVariable(value = "courier") Courier courier){
        deliverySessionService.finishSession(user, courier);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
    @PatchMapping("/api/v1/deliverySession/{deliverySession}")
    public ResponseEntity<Object> finishSession(@AuthenticationPrincipal User user,
                                                @PathVariable DeliverySession deliverySession){
        deliverySessionService.finishSession(user, deliverySession);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COURIER')")
    @GetMapping("/api/v1/profile/deliverySessions")
    public ResponseEntity<List<DeliverySessionInfoDTO>> getSessionsHistory(@AuthenticationPrincipal User user,
                                                                           @PageableDefault(sort = { "start_time" },
                                                                           direction = Sort.Direction.DESC) Pageable pageable){
        return new ResponseEntity<>(deliverySessionService.getSessionsHistory(user, pageable), HttpStatus.OK);
    }
}
