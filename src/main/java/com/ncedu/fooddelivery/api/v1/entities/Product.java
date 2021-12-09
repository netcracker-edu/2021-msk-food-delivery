package com.ncedu.fooddelivery.api.v1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Data
@Table(name = "products")
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="product")
    @SequenceGenerator(name="product", sequenceName = "products_product_id_seq", allocationSize = 1)
    @Column(name = "product_id")
    private Long id;
    private String name;
    private String description;

    @Column(name = "picture_id")
    private UUID pictureUUID;
    private Integer weight;
    private String composition;
    @Column(name = "expiration_days")
    private Short expirationDays;
    @Column(name = "in_showcase")
    private Boolean inShowcase;
    private Double price;
    private Double discount; //default value

    public Product(){}
}
