package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.product.ProductDTO;
import com.ncedu.fooddelivery.api.v1.entities.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    public Product getProductById(Long productId);
    public ProductDTO getProductDTOById(Long productId);
    public List<ProductDTO> searchProducts(String phrase, Pageable pageable);
    public List<ProductDTO> searchProductsInShowcase(String phrase, Pageable pageable);
}
