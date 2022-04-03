package com.ncedu.fooddelivery.api.v1.errors.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCostChangedSubError extends ApiSubError{
    private Double overallCost, discount, highDemandCoeff;
}
