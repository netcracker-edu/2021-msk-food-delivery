package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.WarehouseInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAccessDeniedException;
import com.ncedu.fooddelivery.api.v1.services.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WarehouseController {
    @Autowired
    WarehouseService warehouseService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @GetMapping(path = "/api/v1/warehouse/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WarehouseInfoDTO> getById(@PathVariable Long id, @AuthenticationPrincipal User authedUser){
        if(Role.isMODERATOR(authedUser.getRole().toString())){
            if(!authedUser.getModerator().getWarehouseId().equals(id)){
                throw new CustomAccessDeniedException();
            }
        }
        WarehouseInfoDTO warehouseInfoDTO = this.warehouseService.getWarehouseInfoDTOById(id);
        if(warehouseInfoDTO == null) throw new NotFoundEx(String.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body(warehouseInfoDTO);
    }
}
