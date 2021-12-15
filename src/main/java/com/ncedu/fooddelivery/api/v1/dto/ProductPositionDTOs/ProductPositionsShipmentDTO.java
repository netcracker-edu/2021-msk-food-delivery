package com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProductPositionsShipmentDTO {
    @NotEmpty
    @Valid
    @JsonProperty(value = "productPositions")
    private List<ProductPositionAmountPair> positionAmountPairs;

    @Data
    public static class ProductPositionAmountPair{

        @Valid
        @JsonProperty(value = "productPositionId")
        @NotNull
        @Min(value = 1L) @Max(value = Long.MAX_VALUE)
        Long id;


        @Valid
        @JsonProperty(value = "amount")
        @NotNull
        @Min(value = 1L) @Max(value = Long.MAX_VALUE)
        Integer amount;
    }
}
