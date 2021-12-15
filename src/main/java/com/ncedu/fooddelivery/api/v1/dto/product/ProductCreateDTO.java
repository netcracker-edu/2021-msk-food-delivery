package com.ncedu.fooddelivery.api.v1.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ProductCreateDTO {

    @NotNull
    @Size(max = 50)
    private String name;
    private String description;
    private UUID pictureUUID;
    @NotNull
    @Min(1)
    private Integer weight;
    private String composition;
    private Short expirationDays;
    @NotNull
    private Boolean inShowcase;
    @NotNull
    @Min(1)
    private Double price;
    @NotNull
    @Min(0)
    private Double discount;

    public ProductCreateDTO() {};
}
