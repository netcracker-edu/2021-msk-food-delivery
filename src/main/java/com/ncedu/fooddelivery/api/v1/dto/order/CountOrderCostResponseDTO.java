package com.ncedu.fooddelivery.api.v1.dto.order;

import lombok.Data;

@Data
public class CountOrderCostResponseDTO {
    private Double overallCost;
    private Double discount;
    private Double highDemandCoeff;

    public CountOrderCostResponseDTO(Double overallCost, Double discount, Double highDemandCoeff) {
        this.overallCost = overallCost;
        this.discount = discount;
        this.highDemandCoeff = highDemandCoeff;
    }
}
