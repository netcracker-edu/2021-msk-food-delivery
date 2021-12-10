package com.ncedu.fooddelivery.api.v1.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ProductUpdateDTO {

    @Size(max = 50)
    private String name;
    private String description;
    private UUID pictureUUID;
    @Min(1)
    private Integer weight;
    private String composition;
    private Short expirationDays;
    @Min(1)
    private Double price;
    @Min(0)
    private Double discount;

    public ProductUpdateDTO() {};
}
