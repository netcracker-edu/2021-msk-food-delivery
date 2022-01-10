package com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPosition;
import lombok.Data;

import java.util.List;

@Data
public class ProductPositionsFromOrderDTO {
    @JsonProperty(value = "productPositions")
    private List<ProductPositionsFromOrderDTO.ProductPositionAmountPair> productPositions;

    @Data
    public static class ProductPositionAmountPair{
        @JsonProperty(value = "amount")
        private Integer amount;

        @JsonProperty(value = "productPosition")
        private ProductPosition productPosition;


        public ProductPositionAmountPair(ProductPosition productPosition, Integer amount) {
            this.productPosition = productPosition;
            this.amount = amount;
        }
    }
}
