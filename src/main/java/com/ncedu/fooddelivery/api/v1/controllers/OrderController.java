package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.order.*;
import com.ncedu.fooddelivery.api.v1.entities.OrderStatus;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.IncorrectUserRoleRequestException;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAccessDeniedException;
import com.ncedu.fooddelivery.api.v1.services.OrderService;
import com.ncedu.fooddelivery.api.v1.services.UserService;
import com.ncedu.fooddelivery.api.v1.specifications.OrderSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @GetMapping("/api/v1/orders")
    public ResponseEntity<List<OrderInfoDTO>> findFiltered(
            @RequestParam(name = "clientId", required = false) @Min(value = 1) @Max(value = Long.MAX_VALUE) Long clientId,
            @RequestParam(name = "warehouseId", required = false) @Min(value = 1) @Max(value = Long.MAX_VALUE) Long warehouseId,
            @RequestParam(name = "courierId", required = false) @Min(value = 1) @Max(value = Long.MAX_VALUE) Long courierId,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "status", required = false) OrderStatus status,
            @RequestParam(name = "dateStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
            @RequestParam(name = "dateEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd,
            @RequestParam(name = "overallCost", required = false) @Digits(integer = 5, fraction = 2) @DecimalMin(value = "0.0", inclusive = false) BigDecimal overallCost,
            @RequestParam(name = "highDemandCoeff", required = false) @Digits (integer = 1, fraction = 2) @DecimalMin("1.0") @DecimalMax("3.0") BigDecimal highDemandCoeff,
            @RequestParam(name = "discount", required = false)  @Digits(integer = 5, fraction = 2) @DecimalMin(value = "0.0") BigDecimal discount,
            @RequestParam(name = "promoCodeId", required = false) @Min(value = 1) @Max(value = Long.MAX_VALUE) Long promoCodeId,
            @RequestParam(name = "clientRating", required = false) @Digits(integer = 1, fraction = 2) @DecimalMin(value = "0.0", inclusive = false) BigDecimal clientRating,
            @RequestParam(name = "deliveryRating", required = false) @Digits (integer = 1, fraction = 2) @DecimalMin(value = "0.0", inclusive = false) BigDecimal deliveryRating,

            @AuthenticationPrincipal User user,
            Pageable pageable){

        userService.checkIsUserLocked(user);

        List<OrderInfoDTO> filteredOrders;

        if(user.getRole() == Role.MODERATOR){
            Long moderatorWarehouseId = user.getModerator().getWarehouseId();
            if(warehouseId != null){
                if(!warehouseId.equals(moderatorWarehouseId)) throw new CustomAccessDeniedException();
            }
            Specification<Order> spec = OrderSpecifications.getFilterSpecification(
                    clientId, moderatorWarehouseId, courierId, address, status, dateStart, dateEnd,
                    overallCost, highDemandCoeff, discount, promoCodeId, clientRating,
                    deliveryRating
            );
            filteredOrders = orderService.findFiltered(spec, pageable);

        } else {
            Specification<Order> spec = OrderSpecifications.getFilterSpecification(
                    clientId, null, courierId, address, status, dateStart, dateEnd,
                    overallCost, highDemandCoeff, discount, promoCodeId, clientRating,
                    deliveryRating
            );
            filteredOrders = orderService.findFiltered(spec, pageable);
        }

        return new ResponseEntity<>(filteredOrders, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @GetMapping("/api/v1/user/{id}/orders")
    public ResponseEntity<List<OrderInfoDTO>> getOrdersHistory(@AuthenticationPrincipal User user,
                                                               @PathVariable @Min(value = 1) @Max(value = Long.MAX_VALUE) Long id,
                                                               @PageableDefault(sort = { "date_start" }, direction = Sort.Direction.DESC) Pageable pageable){
        userService.checkIsUserLocked(user);
        User targetUser = userService.getUserById(id);
        if(targetUser == null) throw new NotFoundEx(String.valueOf(id));
        if(targetUser.getRole() == Role.ADMIN || targetUser.getRole() == Role.MODERATOR) throw new IncorrectUserRoleRequestException();
        List<OrderInfoDTO> ordersHistory;
        if(user.getRole() == Role.ADMIN){
            ordersHistory = orderService.getOrdersHistory(targetUser, pageable);
        } else {
            Long moderatorWarehouseId = user.getModerator().getWarehouseId();
            ordersHistory = orderService.getOrdersHistory(targetUser, pageable).stream().filter(order -> order.getWarehouse().getId().equals(moderatorWarehouseId)).collect(Collectors.toList());
        }
        return new ResponseEntity<>(ordersHistory, HttpStatus.OK);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/v1/user/orders")
    public ResponseEntity<List<OrderInfoDTO>> getMyOrdersHistory(@AuthenticationPrincipal User user,
                                                                 @PageableDefault(sort = { "date_start" }, direction = Sort.Direction.DESC) Pageable pageable){
        if(user.getRole() == Role.ADMIN || user.getRole() == Role.MODERATOR) throw new IncorrectUserRoleRequestException();
        List<OrderInfoDTO> ordersHistory = orderService.getOrdersHistory(user, pageable);
        return new ResponseEntity<>(ordersHistory, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/v1/order/price")
    public ResponseEntity<CountOrderCostResponseDTO> countOrderCost(@AuthenticationPrincipal User user,
                                                                    @Valid @RequestBody CountOrderCostRequestDTO requestDTO){
        CountOrderCostResponseDTO responseDTO = orderService.countOrderCost(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping("/api/v1/order")
    public ResponseEntity<CreatedOrdersIdDTO> createOrder(@AuthenticationPrincipal User user,
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
    @PatchMapping("/api/v1/order/{id}/cancellation")
    public ResponseEntity<?> cancelOrder(@AuthenticationPrincipal User user,
                                                     @Min(value = 1) @Max(value = Long.MAX_VALUE) @PathVariable Long id){
        orderService.cancelOrder(id, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/api/v1/order/{id}/status")
    public ResponseEntity<?> changeOrderStatus(@AuthenticationPrincipal User user,
                                               @Min(value = 1) @Max(value = Long.MAX_VALUE) @PathVariable Long id){
        orderService.changeOrderStatus(id, user);
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
                                                 @RequestParam @Digits(integer = 1, fraction = 2) @DecimalMin(value = "0.0") @DecimalMax(value = "5.0") BigDecimal newRating
                                                 ){

        orderService.changeDeliveryRating(orderId, newRating, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COURIER')")
    @PatchMapping("/api/v1/order/{orderId}/clientRating")
    public ResponseEntity<?> changeClientRating(@AuthenticationPrincipal User user,
                                                 @Min(value = 1) @Max(value = Long.MAX_VALUE) @PathVariable Long orderId,
                                                 @RequestParam @Digits(integer = 1, fraction = 2) @DecimalMin(value = "0.0") @DecimalMax(value = "5.0") BigDecimal newRating
    ){

        orderService.changeClientRating(orderId, newRating, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
