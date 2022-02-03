package com.ncedu.fooddelivery.api.v1.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ncedu.fooddelivery.api.v1.dto.CoordsDTO;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Data
public class CountOrderCostRequestDTO {

    @NotNull
    private Long warehouseId;

    @NotNull
    private CoordsDTO geo;

    @NotNull
    @JsonProperty(value = "products")
    private HashMap<Long, Integer> productAmountPairs;

}
