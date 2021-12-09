package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.SearchDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductDTO;
import com.ncedu.fooddelivery.api.v1.entities.Product;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.repos.ProductRepo;
import com.ncedu.fooddelivery.api.v1.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/api/v1/products/search")
    public List<ProductDTO> searchProducts(
            @Valid @RequestBody SearchDTO searchDTO,
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal User authedUser) {
        String phrase = searchDTO.getPhrase();
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
