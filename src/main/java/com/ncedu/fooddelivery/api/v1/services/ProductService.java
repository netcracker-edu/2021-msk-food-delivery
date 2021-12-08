package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.product.ProductDTO;
import com.ncedu.fooddelivery.api.v1.entities.Product;

public interface ProductService {
    public Product getProductById(Long productId);
    public ProductDTO getProductDTOById(Long productId);
}
