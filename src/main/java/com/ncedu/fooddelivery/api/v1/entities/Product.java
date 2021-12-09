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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String name;
    private String description;

    @Column(name = "picture_id")
    private UUID pictureUUID;

    @NotBlank
    private Integer weight;
    @Size(max = 250)
    private String composition;

    @Column(name = "expiration_days")
    private Short expirationDays;

    @NotBlank
    @Column(name = "in_showcase")
    private Boolean inShowcase;

    @NotBlank
    @Min(1)
    private Double price;
    @NotNull
    private Double discount; //default value

    public Product(){}
}
