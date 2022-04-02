package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.areCreatedDTO;
import com.ncedu.fooddelivery.api.v1.dto.order.*;
import com.ncedu.fooddelivery.api.v1.entities.DeliverySession;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.entities.order.Order;
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
    @GetMapping("/api/v1/user/{targetUser}/orders")
    public ResponseEntity<List<OrderInfoDTO>> getOrdersHistory(@AuthenticationPrincipal User authedUser,
                                                               @PathVariable User targetUser,
                                                               @PageableDefault(sort = { "date_start" },
                                                               direction = Sort.Direction.DESC) Pageable pageable){

        return new ResponseEntity<>(orderService.getOrdersHistory(authedUser, targetUser, pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('CLIENT', 'COURIER')")
    @GetMapping("/api/v1/profile/orders")
    public ResponseEntity<List<OrderInfoDTO>> getMyOrdersHistory(@AuthenticationPrincipal User user,
                                                                 @PageableDefault(sort = { "date_start" },
                                                                 direction = Sort.Direction.DESC) Pageable pageable){

        return new ResponseEntity<>(orderService.getMyOrdersHistory(user, pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COURIER')")
    @GetMapping("/api/v1/deliverySession/{deliverySession}/orders")
    public ResponseEntity<List<OrderInfoDTO>> getOrdersFromSession(@AuthenticationPrincipal User user,
                                                                   @PathVariable DeliverySession deliverySession){
        return new ResponseEntity<>(orderService.getOrdersFromDeliverySession(user, deliverySession),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COURIER')")
    @GetMapping("/api/v1/order")
    public ResponseEntity<OrderInfoDTO> getCurrentOrder(@AuthenticationPrincipal User user){
        OrderInfoDTO orderInfoDTO = orderService.getCurrentOrder(user);
        if(orderInfoDTO == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(orderInfoDTO, HttpStatus.OK);
    }

    @GetMapping("/api/v1/order/price")
    public ResponseEntity<CountOrderCostResponseDTO> countOrderCost(@Valid @RequestBody CountOrderCostRequestDTO requestDTO){
        CountOrderCostResponseDTO responseDTO = orderService.countOrderCost(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping("/api/v1/order")
    public ResponseEntity<areCreatedDTO> createOrder(@AuthenticationPrincipal User user,
                                                     @Valid @RequestBody CreateOrderDTO dto){
        return new ResponseEntity<>(orderService.createOrder(dto, user), HttpStatus.OK);
    }

    @GetMapping("/api/v1/order/{order}")
    public ResponseEntity<OrderInfoDTO> getOrderInfo(@AuthenticationPrincipal User user,
                                                     @PathVariable Order order){
        return new ResponseEntity<>(orderService.getOrderInfo(order, user), HttpStatus.OK);
    }

    @PatchMapping("/api/v1/order/{order}/status")
    public ResponseEntity<?> changeOrderStatus(@AuthenticationPrincipal User user,
                                               @PathVariable Order order,
                                               @Valid @RequestBody ChangeOrderStatusDTO dto
                                               ){
        orderService.changeOrderStatus(order, user, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
    @PatchMapping("/api/v1/order/{order}/courier")
    public ResponseEntity<?> replaceCourier(@AuthenticationPrincipal User user,
                                            @PathVariable Order order){
        orderService.replaceCourier(order, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @PatchMapping("/api/v1/order/{order}/courierRating")
    public ResponseEntity<?> changeCourierRating(@AuthenticationPrincipal User user,
                                                 @PathVariable Order order,
                                                 @Valid @RequestBody ChangeRatingDTO dto){

        orderService.changeDeliveryRating(order, dto, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COURIER')")
    @PatchMapping("/api/v1/order/{order}/clientRating")
    public ResponseEntity<?> changeClientRating(@AuthenticationPrincipal User user,
                                                @PathVariable Order order,
                                                @Valid @RequestBody ChangeRatingDTO dto
                                                ){
        orderService.changeClientRating(order, dto, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('CLIENT', 'COURIER')")
    @GetMapping("/api/v1/profile/orders/amount")
    public ResponseEntity<OrdersAmountDTO> getOrdersAmount(@AuthenticationPrincipal User user){
        return new ResponseEntity<OrdersAmountDTO>(orderService.getOrdersAmount(user), HttpStatus.OK);
    }
}
