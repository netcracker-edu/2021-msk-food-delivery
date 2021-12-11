package com.ncedu.fooddelivery.api.v1.entities;


import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Component
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product")
    @SequenceGenerator(name = "product", sequenceName = "products_product_id_seq", allocationSize = 1)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "picture_id")
    private Long pictureUUID;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "composition")
    private String composition;

    @Column(name = "expiration_days")
    private Short expirationDays;

    @Column(name = "in_showcase")
    private Boolean inShowcase;

    @Column(name = "price")
    private Float price;

    @Column(name = "discount")
    private Float discount;

    public Product(){}

    public Product(Long id, String name, String description, Long pictureUUID, Integer weight, String composition, Short expirationDays, Boolean inShowcase, Float price, Float discount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pictureUUID = pictureUUID;
        this.weight = weight;
        this.composition = composition;
        this.expirationDays = expirationDays;
        this.inShowcase = inShowcase;
        this.price = price;
        this.discount = discount;
    }
}
