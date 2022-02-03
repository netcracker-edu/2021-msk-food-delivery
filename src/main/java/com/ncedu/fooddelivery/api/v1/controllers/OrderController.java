package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.AreCreatedDTO;
import com.ncedu.fooddelivery.api.v1.dto.order.*;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Validated
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @GetMapping("/api/v1/orders")
    public ResponseEntity<List<OrderInfoDTO>> findFiltered(
            @Valid OrderFilterDTO dto,
            @AuthenticationPrincipal User user,
            Pageable pageable){

        return new ResponseEntity<>(orderService.findFiltered(user, dto, pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @GetMapping("/api/v1/user/{id}/orders")
    public ResponseEntity<List<OrderInfoDTO>> getOrdersHistory(@AuthenticationPrincipal User user,
                                                               @PathVariable @Min(value = 1) @Max(value = Long.MAX_VALUE) Long id,
                                                               @PageableDefault(sort = { "date_start" },
                                                               direction = Sort.Direction.DESC) Pageable pageable){



        return new ResponseEntity<>(orderService.getOrdersHistory(user, id, pageable), HttpStatus.OK);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/v1/user/orders")
    public ResponseEntity<List<OrderInfoDTO>> getMyOrdersHistory(@AuthenticationPrincipal User user,
                                                                 @PageableDefault(sort = { "date_start" },
                                                                 direction = Sort.Direction.DESC) Pageable pageable){

        return new ResponseEntity<>(orderService.getMyOrdersHistory(user, pageable), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/v1/order/price")
    public ResponseEntity<CountOrderCostResponseDTO> countOrderCost(@Valid @RequestBody CountOrderCostRequestDTO requestDTO){
        CountOrderCostResponseDTO responseDTO = orderService.countOrderCost(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping("/api/v1/order")
    public ResponseEntity<AreCreatedDTO> createOrder(@AuthenticationPrincipal User user,
                                                     @Valid @RequestBody CreateOrderDTO dto){
        return new ResponseEntity<>(orderService.createOrder(dto, user), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/v1/order/{id}")
    public ResponseEntity<OrderInfoDTO> getOrderInfo(@AuthenticationPrincipal User user,
                                                     @Min(value = 1) @Max(value = Long.MAX_VALUE) @PathVariable Long id){
        return new ResponseEntity<>(orderService.getOrderInfo(id, user), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/api/v1/order/{id}/status")
    public ResponseEntity<?> changeOrderStatus(@AuthenticationPrincipal User user,
                                               @Min(value = 1) @Max(value = Long.MAX_VALUE) @PathVariable Long id,
                                               @Valid @RequestBody ChangeOrderStatusDTO dto
                                               ){
        orderService.changeOrderStatus(id, user, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
    @PatchMapping("/api/v1/order/{orderId}/courier")
    public ResponseEntity<?> replaceCourier(@AuthenticationPrincipal User user,
                                            @Min(value = 1) @Max(value = Long.MAX_VALUE) @PathVariable Long orderId){
        orderService.replaceCourier(orderId, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @PatchMapping("/api/v1/order/{orderId}/courierRating")
    public ResponseEntity<?> changeCourierRating(@AuthenticationPrincipal User user,
                                                 @Min(value = 1) @Max(value = Long.MAX_VALUE) @PathVariable Long orderId,
                                                 @Valid @RequestBody ChangeRatingDTO dto){

        orderService.changeDeliveryRating(orderId, dto, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COURIER')")
    @PatchMapping("/api/v1/order/{orderId}/clientRating")
    public ResponseEntity<?> changeClientRating(@AuthenticationPrincipal User user,
                                                @Min(value = 1) @Max(value = Long.MAX_VALUE) @PathVariable Long orderId,
                                                @Valid @RequestBody ChangeRatingDTO dto
                                                ){
        orderService.changeClientRating(orderId, dto, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
