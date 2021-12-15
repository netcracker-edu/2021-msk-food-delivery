package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.isCreatedDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductCreateDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductUpdateDTO;
import com.ncedu.fooddelivery.api.v1.entities.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    public Product getProductById(Long productId);
    public ProductDTO getProductDTOById(Long productId);
    public ProductDTO getProductDTOByIdInShowcase(Long productId);
    public isCreatedDTO createProduct(ProductCreateDTO newProduct);
    public void updateProduct(Long id, ProductUpdateDTO updatedProduct);
    public boolean switchInShowcaseStatus(Long id);
    public void deleteProduct(Long id);
    public List<ProductDTO> getProducts(Pageable pageable);
    public List<ProductDTO> getProductsInShowcase(Pageable pageable);
    public List<ProductDTO> searchProducts(String phrase, Pageable pageable);
    public List<ProductDTO> searchProductsInShowcase(String phrase, Pageable pageable);
}
