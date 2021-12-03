package com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProductPositionsShipmentDTO {
    @NotEmpty
    @JsonProperty(value = "productPositions")
    private List<ProductPositionAmountPair> positionAmountPairs;

    @Data
    public static class ProductPositionAmountPair{
        @JsonProperty(value = "productPositionId")
        @NotNull
        @Min(value = 1L) @Max(value = Long.MAX_VALUE)
        Long id;

        @JsonProperty(value = "amount")
        @NotNull
        @Min(value = 1L) @Max(value = Long.MAX_VALUE)
        Integer amount;
    }
}
