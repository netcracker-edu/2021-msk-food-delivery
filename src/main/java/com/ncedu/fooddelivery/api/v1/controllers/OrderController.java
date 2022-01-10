package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.OrderInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.OrderStatus;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.entities.order.OrderNotHierarchical;
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAccessDeniedException;
import com.ncedu.fooddelivery.api.v1.services.OrderService;
import com.ncedu.fooddelivery.api.v1.specifications.OrderSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Validated
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @GetMapping("/api/v1/orders")
    public ResponseEntity<List<OrderInfoDTO>> findFiltered(
            @RequestParam(name = "clientId", required = false) @Min(value = 1) @Max(value = Long.MAX_VALUE) Long clientId,
            @RequestParam(name = "warehouseId", required = false) @Min(value = 1) @Max(value = Long.MAX_VALUE) Long warehouseId,
            @RequestParam(name = "courierId", required = false) @Min(value = 1) @Max(value = Long.MAX_VALUE) Long courierId,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "status", required = false) OrderStatus status,
            @RequestParam(name = "dateStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dateStart,
            @RequestParam(name = "dateEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dateEnd,
            @RequestParam(name = "overallCost", required = false) @Digits(integer = 5, fraction = 2) @DecimalMin(value = "0.0", inclusive = false) BigDecimal overallCost,
            @RequestParam(name = "highDemandCoeff", required = false) @Digits (integer = 1, fraction = 2) @DecimalMin("1.0") @DecimalMax("3.0") BigDecimal highDemandCoeff,
            @RequestParam(name = "discount", required = false)  @Digits(integer = 5, fraction = 2) @DecimalMin(value = "0.0") BigDecimal discount,
            @RequestParam(name = "promoCodeId", required = false) @Min(value = 1) @Max(value = Long.MAX_VALUE) Long promoCodeId,
            @RequestParam(name = "clientRating", required = false) @Digits(integer = 1, fraction = 2) @DecimalMin(value = "0.0", inclusive = false) BigDecimal clientRating,
            @RequestParam(name = "deliveryRating", required = false) @Digits (integer = 1, fraction = 2) @DecimalMin(value = "0.0", inclusive = false) BigDecimal deliveryRating,

            @AuthenticationPrincipal User user,
            Pageable pageable){

        List<OrderInfoDTO> filteredOrders;

        if(user.getRole() == Role.MODERATOR){
            Long moderatorWarehouseId = user.getModerator().getWarehouseId();
            if(warehouseId != null){
                if(!warehouseId.equals(moderatorWarehouseId)) throw new CustomAccessDeniedException();
            }
            Specification<OrderNotHierarchical> spec = OrderSpecifications.getFilterSpecification(
                    clientId, moderatorWarehouseId, courierId, address, status, dateStart, dateEnd,
                    overallCost, highDemandCoeff, discount, promoCodeId, clientRating,
                    deliveryRating
            );
            filteredOrders = orderService.findFiltered(spec, pageable);

        } else {
            Specification<OrderNotHierarchical> spec = OrderSpecifications.getFilterSpecification(
                    clientId, null, courierId, address, status, dateStart, dateEnd,
                    overallCost, highDemandCoeff, discount, promoCodeId, clientRating,
                    deliveryRating
            );
            filteredOrders = orderService.findFiltered(spec, pageable);
        }

        return new ResponseEntity<>(filteredOrders, HttpStatus.OK);
    }
}
