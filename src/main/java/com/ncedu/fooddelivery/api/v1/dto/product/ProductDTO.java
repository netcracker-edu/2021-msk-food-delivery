package com.ncedu.fooddelivery.api.v1.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private UUID pictureUUID;
    private Integer weight;
    private String composition;
    private Short expirationDays;
    private Boolean inShowcase;
    private Double price;
    private Double discount;

    public ProductDTO() {};
}
