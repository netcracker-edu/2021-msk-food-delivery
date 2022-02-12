package com.ncedu.fooddelivery.api.v1.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountOrderCostResponseDTO {
    private Double overallCost;
    private Double discount;
    private Double highDemandCoeff;
}
