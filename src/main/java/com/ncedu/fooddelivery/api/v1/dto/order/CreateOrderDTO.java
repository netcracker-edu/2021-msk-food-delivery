package com.ncedu.fooddelivery.api.v1.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ncedu.fooddelivery.api.v1.dto.CoordsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class CreateOrderDTO {

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "99999.99")
    private Double overallCost;

    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "99999.99")
    private Double discount;

    @NotNull
    @DecimalMin(value = "1.0")
    @DecimalMax(value = "2.99")
    @JsonProperty(value = "highDemandCoeff")
    private Double highDemandCoeff;

    @NotNull
    private Long warehouseId;

    @NotNull
    @JsonProperty(value = "geo")
    private CoordsDTO geo;

    @NotNull
    @JsonProperty(value = "products")
    private HashMap<Long, Integer> productAmountPairs;

    @NotNull
    @Size(min = 10, max = 100)
    private String address;

    public CreateOrderDTO(){

    }
}
