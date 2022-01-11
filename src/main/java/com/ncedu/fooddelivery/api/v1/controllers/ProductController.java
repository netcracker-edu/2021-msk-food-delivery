package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.SearchDTO;
import com.ncedu.fooddelivery.api.v1.dto.isCreatedDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductCreateDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductUpdateDTO;
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
    //TODO: add unit tests

    @Autowired
    ProductService productService;

    @GetMapping("/api/v1/product/{id}")
    public ProductDTO getProductById(
            @PathVariable Long id,
            @AuthenticationPrincipal User authedUser) {
        log.debug(authedUser.getEmail() + " GET /api/v1/product/" + id);
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
        log.debug("POST /api/v1/product");
        isCreatedDTO createdDTO = productService.createProduct(newProduct);
        log.debug("Created product with id: " + createdDTO.getId());
        return createdDTO;
    }

    @DeleteMapping("/api/v1/product/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long id) {
        log.debug("DELETE /api/v1/product/" + id);
        productService.deleteProduct(id);
        log.debug("Deleted product with id: " + id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/api/v1/product/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateDTO updatedProduct) {
        log.debug("PUT /api/v1/product/" + id);
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
        log.debug("PATCH /api/v1/product/" + id + "/inShowcase");
        boolean inShowcase = productService.switchInShowcaseStatus(id);
        log.debug("New value inShowcase: '" + inShowcase + "' for product: " + id);
        return createModifyResponse("inShowcase", inShowcase);
    }

    private ResponseEntity<?> createModifyResponse(String fieldName, boolean value) {
        Map<String, Boolean> response = new HashMap<>();
        response.put(fieldName, value);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }


    //TODO: modify with warehouse and product positions limits
    @GetMapping("/api/v1/products")
    public List<ProductDTO> getProducts(
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable,
            @AuthenticationPrincipal User authedUser) {
        log.debug("GET /api/v1/products PAGE=" + pageable.getPageNumber() + " SIZE=" + pageable.getPageSize());
        String authedUserRole = authedUser.getRole().name();
        List<ProductDTO> productsDTO = null;

        if (Role.isCLIENT(authedUserRole)) {
            productsDTO = productService.getProductsInShowcase(pageable);
        } else {
            productsDTO = productService.getProducts(pageable);
        }
        return productsDTO;
    }

    //TODO: modify with warehouse and product positions limits
    @GetMapping("/api/v1/products/search")
    public List<ProductDTO> searchProducts(
            @Valid @RequestBody SearchDTO searchDTO,
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal User authedUser) {
        String phrase = searchDTO.getPhrase();
        log.debug("GET /api/v1/products/search with phrase:'" + phrase + "' PAGE=" + pageable.getPageNumber() + " SIZE=" + pageable.getPageSize());
        List<ProductDTO> productsDTO;
        String authedUserRole = authedUser.getRole().name();

        if (Role.isCLIENT(authedUserRole)) {
            productsDTO = productService.searchProductsInShowcase(phrase, pageable);
        } else {
            productsDTO = productService.searchProducts(phrase, pageable);
        }
        return productsDTO;
    }

}
