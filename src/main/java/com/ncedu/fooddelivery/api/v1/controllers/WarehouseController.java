package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.warehouseDTOs.WarehouseInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAccessDeniedException;
import com.ncedu.fooddelivery.api.v1.services.WarehouseService;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.List;

@Validated
@RestController
public class WarehouseController {
    @Autowired
    WarehouseService warehouseService;

    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/api/v1/warehouse/nearest")
    public ResponseEntity<WarehouseInfoDTO> getNearestWarehouse(@RequestParam(required = true) @DecimalMin(value = "-90.0", inclusive = true) @DecimalMax(value = "90.0", inclusive = true) BigDecimal lat,
                                                                     @RequestParam(required = true) @DecimalMin(value = "-180.0", inclusive = false) @DecimalMax(value = "180.0", inclusive = false) BigDecimal lon){

        Point geo = geometryFactory.createPoint(new Coordinate(lon.doubleValue(), lat.doubleValue()));
        WarehouseInfoDTO nearestWarehouse = warehouseService.getNearestWarehouse(geo);
        return new ResponseEntity<>(nearestWarehouse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = "/api/v1/warehouses/active")
    public ResponseEntity<List<WarehouseInfoDTO>> getActiveWarehouses(@AuthenticationPrincipal User user){
        return new ResponseEntity<>(warehouseService.getActiveWarehouses(), HttpStatus.OK);
    }
}
