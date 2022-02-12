package com.ncedu.fooddelivery.api.v1.dto.order;

import lombok.Data;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Data
public class ChangeRatingDTO {

    @Digits(integer = 1, fraction = 2)
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    private BigDecimal rating;

    public ChangeRatingDTO(BigDecimal rating) {
        this.rating = rating;
    }

    public ChangeRatingDTO(){}
}
