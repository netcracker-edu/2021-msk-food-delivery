package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.CoordsDTO;
import com.ncedu.fooddelivery.api.v1.dto.SearchDTO;
import com.ncedu.fooddelivery.api.v1.dto.file.FileLinkDTO;
import com.ncedu.fooddelivery.api.v1.dto.isCreatedDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductCreateDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductUpdateDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.SearchProductDTO;
import com.ncedu.fooddelivery.api.v1.entities.Product;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ProductController {
    //TODO: maybe add filters

    @Autowired
    ProductService productService;

    @GetMapping("/api/v1/product/{id}")
    public ProductDTO getProductById(
            @PathVariable Long id,
            @AuthenticationPrincipal User authedUser) {
        String authedUserRole = authedUser.getRole().name();
        if (Role.isCLIENT(authedUserRole)) {
            return productService.getProductDTOByIdInShowcase(id);
        }
        return productService.getProductDTOById(id);
    }

    @PostMapping("/api/v1/product")
    @PreAuthorize("hasAuthority('ADMIN')")
    public isCreatedDTO createProduct(
            @Valid @RequestBody ProductCreateDTO newProduct) {
        isCreatedDTO createdDTO = productService.createProduct(newProduct);
        log.debug("Created product with id: " + createdDTO.getId());
        return createdDTO;
    }

    @DeleteMapping("/api/v1/product/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long id) {
        productService.deleteProduct(id);
        log.debug("Deleted product with id: " + id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/api/v1/product/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateDTO updatedProduct) {
        if (updatedProduct == null) {
            log.error("Empty ProductUpdateDTO was sent for product: " + id);
            throw new NullPointerException();
        }
        productService.updateProduct(id, updatedProduct);
        log.debug("Updated product with id: " + id);
        return createModifyResponse("isModified", true);
    }

    @PatchMapping("/api/v1/product/{id}/inShowcase")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> switchInShowcaseStatus(
            @PathVariable Long id) {
        boolean inShowcase = productService.switchInShowcaseStatus(id);
        log.debug("New value inShowcase: '" + inShowcase + "' for product: " + id);
        return createModifyResponse("inShowcase", inShowcase);
    }

    private ResponseEntity<?> createModifyResponse(String fieldName, boolean value) {
        Map<String, Boolean> response = new HashMap<>();
        response.put(fieldName, value);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/api/v1/product/{product}/picture")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ProductDTO addPicture(
            @PathVariable Product product,
            @RequestBody FileLinkDTO fileLinkDTO) {
        ProductDTO productDTO = productService.addPicture(product, fileLinkDTO.getFileUuid());
        return productDTO;
    }

    @DeleteMapping("/api/v1/product/{product}/picture")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deletePicture(
            @PathVariable Product product,
            @AuthenticationPrincipal User authedUser) {
        productService.deletePicture(product, authedUser);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/api/v1/products")
    public List<ProductDTO> getProducts(
            @Valid @RequestBody CoordsDTO coordinates,
            @PageableDefault(sort = { "product_id" }, direction = Sort.Direction.ASC) Pageable pageable,
            @AuthenticationPrincipal User authedUser) {
        String authedUserRole = authedUser.getRole().name();
        if (Role.isCLIENT(authedUserRole)) {
            return productService.getProductsInShowcase(coordinates, pageable);
        }
        return productService.getProducts(coordinates, pageable);
    }

    @PostMapping("/api/v1/products")
    public List<ProductDTO> getWithPostProducts(
            @Valid @RequestBody CoordsDTO coordinates,
            @PageableDefault(sort = { "product_id" }, direction = Sort.Direction.ASC) Pageable pageable,
            @AuthenticationPrincipal User authedUser) {
        String authedUserRole = authedUser.getRole().name();
        if (Role.isCLIENT(authedUserRole)) {
            return productService.getProductsInShowcase(coordinates, pageable);
        }
        return productService.getProducts(coordinates, pageable);
    }

    @GetMapping("/api/v1/products/search")
    public List<ProductDTO> searchProducts(
            @Valid @RequestBody SearchProductDTO searchDTO,
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal User authedUser) {
        String authedUserRole = authedUser.getRole().name();
        if (Role.isCLIENT(authedUserRole)) {
            return productService.searchProductsInShowcase(searchDTO, pageable);
        }
        return productService.searchProducts(searchDTO, pageable);
    }
}
