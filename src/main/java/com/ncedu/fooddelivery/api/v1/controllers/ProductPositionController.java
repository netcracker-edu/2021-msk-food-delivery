package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs.AcceptSupplyDTO;
import com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs.ProductPositionInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs.ProductPositionsShipmentDTO;
import com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs.UpdatePaymentStatusDTO;
import com.ncedu.fooddelivery.api.v1.dto.isCreatedDTO;
import com.ncedu.fooddelivery.api.v1.entities.Order;
import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPosition;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPositionNotHierarchical;
import com.ncedu.fooddelivery.api.v1.errors.notfound.OrderNotFoundException;
import com.ncedu.fooddelivery.api.v1.errors.notfound.ProductPositionNotFoundException;
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAccessDeniedException;
import com.ncedu.fooddelivery.api.v1.services.OrderService;
import com.ncedu.fooddelivery.api.v1.services.ProductPositionService;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.OnTypeMismatch;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductPositionController {

    @Autowired
    ProductPositionService productPositionService;

    @Autowired
    OrderService orderService;

    ProductPositionController(){}

    @GetMapping(path = "/api/v1/productPosition/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<ProductPositionInfoDTO> getFullInfoById(@AuthenticationPrincipal User user, @PathVariable Long id){
        ProductPositionInfoDTO productPositionInfoDTO = productPositionService.getProductPositionInfoDTOById(id);
        if(productPositionInfoDTO == null) throw new ProductPositionNotFoundException(id);
        if(Role.isMODERATOR(user.getRole().toString())){
            if(!user.getModerator().getWarehouseId().equals(productPositionInfoDTO.getWarehouse().getId())){
                throw new CustomAccessDeniedException();
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(productPositionInfoDTO);
    }

    @PostMapping(path = "/api/v1/productPosition")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public isCreatedDTO acceptSupply(@AuthenticationPrincipal User user, @Valid @RequestBody AcceptSupplyDTO acceptSupplyDTO){
        if(Role.isMODERATOR(user.getRole().toString())){
            if(!user.getModerator().getWarehouseId().equals(acceptSupplyDTO.getWarehouseId())){
                throw new CustomAccessDeniedException();
            }
        }
        Long id = productPositionService.acceptSupply(acceptSupplyDTO);
        isCreatedDTO isCreated = new isCreatedDTO();
        isCreated.setId(id);
        return isCreated;
    }

    @DeleteMapping("/api/v1/productPosition/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> deleteProductPosition(@PathVariable Long id, @AuthenticationPrincipal User user){
        ProductPosition productPositionToDelete = productPositionService.getProductPosition(id);
        if(productPositionToDelete == null) throw new ProductPositionNotFoundException(id);
        if(Role.isMODERATOR(user.getRole().toString())){
            if(!user.getModerator().getWarehouseId().equals(productPositionToDelete.getWarehouse().getId())){
                throw new CustomAccessDeniedException();
            }
        }
        boolean deleteResult = productPositionService.deleteProductPosition(id);
        if(deleteResult) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/api/v1/productPosition/{id}/currentAmount")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> nullifyProductPosition(@PathVariable Long id, @AuthenticationPrincipal User user) {
        ProductPosition productPositionToNullify = productPositionService.getProductPosition(id);
        if (productPositionToNullify == null)
            throw new ProductPositionNotFoundException(id);
        if (Role.isMODERATOR(user.getRole().toString())) {
            if (!user.getModerator().getWarehouseId().equals(productPositionToNullify.getWarehouse().getId())) {
                throw new CustomAccessDeniedException();
            }
        }
        productPositionService.nullifyProductPosition(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/api/v1/productPositions/paymentState")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> updatePaymentState(@AuthenticationPrincipal User user, @Valid @RequestBody UpdatePaymentStatusDTO updatePaymentStatusDTO){

        List<ProductPosition> productPositionList = new ArrayList<>();

        for(Long id: updatePaymentStatusDTO.getProductPositions()){
            ProductPosition productPosition = productPositionService.getProductPosition(id);
            if(productPosition == null) throw new ProductPositionNotFoundException(id);
            productPositionList.add(productPosition);
        }

        if (Role.isMODERATOR(user.getRole().toString())) {
            Long moderatorBindedWarehouseId = user.getModerator().getWarehouseId();
            for(ProductPosition productPosition: productPositionList){
                if(!productPosition.getWarehouse().getId().equals(moderatorBindedWarehouseId)) throw new CustomAccessDeniedException();
            }
        }

        productPositionService.updatePaymentStatus(updatePaymentStatusDTO.getProductPositions());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/v1/productPosition/expired")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<ProductPositionInfoDTO>> getExpiredPositions(@AuthenticationPrincipal User user){
        List<ProductPositionInfoDTO> expiredPositions = new ArrayList<>();
        if(Role.isMODERATOR(user.getRole().toString())){
            expiredPositions = productPositionService.getExpiredPositions(user.getModerator().getWarehouseId());
        } else {
            expiredPositions = productPositionService.getExpiredPositions();
        }
        return ResponseEntity.status(HttpStatus.OK).body(expiredPositions);
    }

    // TODO: ask about DTO: should we return exactly ProductPositionDTO?
    @GetMapping("/api/v1/order/{id}/productPositions")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<AbstractMap.SimpleEntry<Integer, ProductPositionInfoDTO>>> getProductPositionsInOrder(@AuthenticationPrincipal User user,
                                                                                                                     @PathVariable Long id){
        Order order = orderService.getOrder(id);
        if(order == null) throw new OrderNotFoundException(id);

        if(user.getRole() == Role.MODERATOR){
            if(!user.getModerator().getWarehouseId().equals(order.getWarehouse().getId())) throw new CustomAccessDeniedException();
        }
        return new ResponseEntity<>(productPositionService.getPositionsFromOrder(order), HttpStatus.OK);
    }

    @GetMapping("/api/v1/productPositions")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<ProductPositionInfoDTO>> findFiltered(@AuthenticationPrincipal User user,
                           @And({
                                   @Spec(path="productId", params="productId", spec=Equal.class, onTypeMismatch= OnTypeMismatch.EXCEPTION),
                                   @Spec(path="warehouseId", params="warehouseId", spec=Equal.class, onTypeMismatch=OnTypeMismatch.EXCEPTION),
                                   @Spec(path="warehouseSection", params="warehouseSection", spec=Equal.class, onTypeMismatch=OnTypeMismatch.EXCEPTION),
                                   @Spec(path="supplyAmount", params="supplyAmount", spec=Equal.class, onTypeMismatch=OnTypeMismatch.EXCEPTION),
                                   @Spec(path="currentAmount", params="currentAmount", spec=Equal.class, onTypeMismatch=OnTypeMismatch.EXCEPTION),
                                   @Spec(path="supplyDate", params="supplyDate", spec=Equal.class, onTypeMismatch=OnTypeMismatch.EXCEPTION),
                                   @Spec(path="supplierInvoice", params="supplierInvoice", spec=Equal.class, onTypeMismatch=OnTypeMismatch.EXCEPTION),
                                   @Spec(path="supplierName", params="supplierName", spec=Equal.class, onTypeMismatch=OnTypeMismatch.EXCEPTION),
                                   @Spec(path="isInvoicePaid", params="isInvoicePaid", spec=Equal.class, onTypeMismatch=OnTypeMismatch.EXCEPTION),
                                   @Spec(path="manufactureDate", params="manufactureDate", spec=Equal.class, onTypeMismatch=OnTypeMismatch.EXCEPTION),
                           }) Specification<ProductPositionNotHierarchical> productPositionSpecification,
                           Pageable pageable){

        List<ProductPositionInfoDTO> filteredPositions = productPositionService.findFiltered(productPositionSpecification, pageable);
        if(user.getRole() == Role.MODERATOR){
            Long moderatorWarehouseId = user.getModerator().getWarehouseId();

            // if moderator tries to use forbidden for him "warehouseId" (not his warehouse id)
            if(filteredPositions.stream().allMatch(position -> position.getWarehouse().getId().equals(moderatorWarehouseId))) throw new CustomAccessDeniedException();

            filteredPositions = filteredPositions.stream().filter(position -> position.getWarehouse().getId().equals(moderatorWarehouseId)).collect(Collectors.toList());
        }
        return ResponseEntity.status(HttpStatus.OK).body(filteredPositions);
    }

    @PatchMapping("/api/v1/order/{id}/productPositions")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> shipProductPositionsFromOrder(@PathVariable(name = "id") Long id,
                                                           @Valid @RequestBody ProductPositionsShipmentDTO productPositionsShipmentDTO,
                                                           @AuthenticationPrincipal User user){
        Order order = orderService.getOrder(id);
        if(order == null) throw new OrderNotFoundException(id);

        if(user.getRole() == Role.MODERATOR){
            if(!user.getModerator().getWarehouseId().equals(order.getWarehouse().getId())) throw new CustomAccessDeniedException();
        }

        productPositionService.shipProductPositions(id, productPositionsShipmentDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}