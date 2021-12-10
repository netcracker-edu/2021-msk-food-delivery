package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.product.ProductCreateDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductUpdateDTO;
import com.ncedu.fooddelivery.api.v1.entities.Product;
import com.ncedu.fooddelivery.api.v1.mappers.ProductMapper;

public class ProductUtils {

    private static ProductMapper productMapper = ProductMapper.INSTANCE;

    public static Product createMilkInShowcase(Long id) {
        final String name = "Milk";
        final String description = "Awesome taste!!!";
        final boolean inShowcase = true;
        final Double price = 100.0;
        return createProduct(id, name, description, inShowcase, price);
    }
    public static Product createMilkNOTinShowcase(Long id) {
        final String name = "Elite Milk";
        final String description = "Awesome taste!!!";
        final boolean inShowcase = false;
        final Double price = 150.0;
        return createProduct(id, name, description, inShowcase, price);
    }

    public static Product createBreadInShowcase(Long id) {
        final String name = "Bread";
        final String description = "Simple taste, economy option";
        final boolean inShowcase = true;
        final Double price = 30.0;
        return createProduct(id, name, description, inShowcase, price);
    }

    public static Product createBreadNOTinShowcase(Long id) {
        final String name = "Elite Bread";
        final String description = "Yummy!!!";
        final boolean inShowcase = false;
        final Double price = 80.0;
        return createProduct(id, name, description, inShowcase, price);
    }

    public  static Product createProduct(Long id, String name,
                                         String description, boolean inShowcase,
                                         Double price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setExpirationDays(Short.valueOf("5"));
        product.setWeight(400);
        product.setPrice(price);
        product.setInShowcase(inShowcase);
        return product;
    }
    public static ProductDTO createProductDTO(Product product) {
        return productMapper.mapToDTO(product);
    }

    public static ProductCreateDTO createProductCreateDTO(Product p) {
        ProductCreateDTO productDTO = new ProductCreateDTO();
        productDTO.setDescription(p.getDescription());
        productDTO.setPrice(p.getPrice());
        productDTO.setName(p.getName());
        productDTO.setInShowcase(p.getInShowcase());
        productDTO.setWeight(p.getWeight());
        productDTO.setExpirationDays(p.getExpirationDays());
        return productDTO;
    }
    public static ProductUpdateDTO createProductUpdateDTO(Product p) {
        ProductUpdateDTO productDTO = new ProductUpdateDTO();
        productDTO.setDescription(p.getDescription());
        productDTO.setPrice(p.getPrice());
        productDTO.setName(p.getName());
        productDTO.setWeight(p.getWeight());
        productDTO.setExpirationDays(p.getExpirationDays());
        return productDTO;
    }


}
