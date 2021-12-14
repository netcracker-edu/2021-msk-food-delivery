package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs.*;
import com.ncedu.fooddelivery.api.v1.dto.isCreatedDTO;
import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPosition;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPositionNotHierarchical;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAccessDeniedException;
import com.ncedu.fooddelivery.api.v1.services.OrderService;
import com.ncedu.fooddelivery.api.v1.services.ProductPositionService;
import com.ncedu.fooddelivery.api.v1.specifications.ProductPositionSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Validated
@RestController
public class ProductPositionController {

    @Autowired
    ProductPositionService productPositionService;

    @Autowired
    OrderService orderService;

    ProductPositionController(){}

    @GetMapping(path = "/api/v1/productPosition/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<ProductPositionInfoDTO> getFullInfoById(@AuthenticationPrincipal User user, @Min(value = 1) @Max(value = Long.MAX_VALUE) @PathVariable Long id){
        ProductPositionInfoDTO productPositionInfoDTO = productPositionService.getProductPositionInfoDTOById(id);
        if(productPositionInfoDTO == null) throw new NotFoundEx(String.valueOf(id));
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
        if(productPositionToDelete == null) throw new NotFoundEx(String.valueOf(id));
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
    public ResponseEntity<?> nullifyProductPosition(@Min(value = 1) @Max(value = Long.MAX_VALUE) @PathVariable Long id, @AuthenticationPrincipal User user) {
        ProductPosition productPositionToNullify = productPositionService.getProductPosition(id);
        if (productPositionToNullify == null)
            throw new NotFoundEx(String.valueOf(id));
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
    public ResponseEntity<?> updatePaymentStatus(@AuthenticationPrincipal User user, @Valid @RequestBody UpdatePaymentStatusDTO updatePaymentStatusDTO){

        List<ProductPosition> productPositionList = new ArrayList<>();

        for(Long id: updatePaymentStatusDTO.getProductPositions()){
            ProductPosition productPosition = productPositionService.getProductPosition(id);
            if(productPosition == null) throw new NotFoundEx(String.valueOf(id));
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
    public ResponseEntity<List<ProductPositionInfoDTO>> getExpiredPositions(@AuthenticationPrincipal User user, Pageable pageable){
        List<ProductPositionInfoDTO> expiredPositions = new ArrayList<>();
        if(Role.isMODERATOR(user.getRole().toString())){
            expiredPositions = productPositionService.getExpiredPositions(user.getModerator().getWarehouseId(), pageable);
        } else {
            expiredPositions = productPositionService.getExpiredPositions(pageable);
        }
        return ResponseEntity.status(HttpStatus.OK).body(expiredPositions);
    }

    @GetMapping("/api/v1/order/{id}/productPositions")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<ProductPositionsFromOrderDTO> getProductPositionsFromOrder(@AuthenticationPrincipal User user,
                                                                                     @Min(value = 1) @Max(value = Long.MAX_VALUE) @PathVariable Long id){
        Order order = orderService.getOrder(id);
        if(order == null) throw new NotFoundEx(String.valueOf(id));

        if(user.getRole() == Role.MODERATOR){
            if(!user.getModerator().getWarehouseId().equals(order.getWarehouse().getId())) throw new CustomAccessDeniedException();
        }
        return new ResponseEntity<ProductPositionsFromOrderDTO>(productPositionService.getPositionsFromOrder(order), HttpStatus.OK);
    }

    @GetMapping("/api/v1/productPositions")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<ProductPositionInfoDTO>> findFiltered(@AuthenticationPrincipal User user,
                           @RequestParam(name = "warehouseId", required = false) @Min(value = 1) @Max(value = Long.MAX_VALUE) Long warehouseId,
                           @RequestParam(name = "productId", required = false) @Min(value = 1) @Max(value = Long.MAX_VALUE) Long productId,
                           @RequestParam(name = "warehouseSection", required = false) String warehouseSection,
                           @RequestParam(name = "supplyAmount", required = false) @Min(value = 1) @Max(value = Integer.MAX_VALUE) Integer supplyAmount,
                           @RequestParam(name = "currentAmount", required = false) @Min(value = 1) @Max(value = Integer.MAX_VALUE) Integer currentAmount,
                           @RequestParam(name = "supplierInvoice", required = false) @Digits(integer = 10, fraction = 2) @DecimalMin(value = "0.0", inclusive = false) BigDecimal supplierInvoice,
                           @RequestParam(name = "supplierName", required = false) String supplierName,
                           @RequestParam(name = "isInvoicePaid", required = false) Boolean isInvoicePaid,
                           @RequestParam(name = "supplyDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date supplyDate,
                           @RequestParam(name = "manufactureDate", required = false)  @DateTimeFormat(pattern="yyyy-MM-dd") Date manufactureDate,
                           Pageable pageable){

        List<ProductPositionInfoDTO> filteredPositions;
        if(user.getRole() == Role.MODERATOR){
            Long moderatorWarehouseId = user.getModerator().getWarehouseId();
            if(warehouseId != null){
                if(!warehouseId.equals(moderatorWarehouseId)) throw new CustomAccessDeniedException();
            }

            Specification<ProductPositionNotHierarchical> spec = ProductPositionSpecifications.getFilterSpecification(
                    productId, moderatorWarehouseId, currentAmount, supplyAmount, manufactureDate, supplierInvoice,
                    supplyDate, supplierName, warehouseSection, isInvoicePaid
            );
            filteredPositions = productPositionService.findFiltered(spec, pageable);

        } else {
            Specification<ProductPositionNotHierarchical> spec = ProductPositionSpecifications.getFilterSpecification(
                    productId, warehouseId, currentAmount, supplyAmount, manufactureDate, supplierInvoice,
                    supplyDate, supplierName, warehouseSection, isInvoicePaid
            );
            filteredPositions = productPositionService.findFiltered(spec, pageable);
        }
        return ResponseEntity.status(HttpStatus.OK).body(filteredPositions);
    }


    @PatchMapping("/api/v1/order/{id}/productPositions/currentAmount")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> shipProductPositionsFromOrder(@Min(value = 1) @Max(value = Long.MAX_VALUE) @PathVariable(name = "id") Long id,
                                                           @Valid @RequestBody ProductPositionsShipmentDTO productPositionsShipmentDTO,
                                                           @AuthenticationPrincipal User user){
        Order order = orderService.getOrder(id);
        if(order == null) throw new NotFoundEx(String.valueOf(id));

        if(user.getRole() == Role.MODERATOR){
            if(!user.getModerator().getWarehouseId().equals(order.getWarehouse().getId())) throw new CustomAccessDeniedException();
        }

        productPositionService.shipProductPositions(id, productPositionsShipmentDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}