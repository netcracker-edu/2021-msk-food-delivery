package com.ncedu.fooddelivery.api.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CoordsDTO {

    @DecimalMin(value = "-90.0", inclusive = true)
    @DecimalMax(value = "90.0", inclusive = true)
    private BigDecimal lat;

    @DecimalMin(value = "-180.0", inclusive = false)
    @DecimalMax(value = "180.0", inclusive = false)
    private BigDecimal lon;

    public CoordsDTO() {};
}