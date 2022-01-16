package com.ncedu.fooddelivery.api.v1.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Validated
public class CountOrderCostRequestDTO {

    @Validated
    @Data
    public static class Geo{

        @DecimalMin(value = "-90.0", inclusive = true)
        @DecimalMax(value = "90.0", inclusive = true)
        BigDecimal lat;

        @DecimalMin(value = "-180.0", inclusive = false)
        @DecimalMax(value = "180.0", inclusive = false)
        BigDecimal lon;

    }

    @Data
    public static class ProductAmountPair{

        @Valid
        @JsonProperty(value = "productId")
        @NotNull
        @Min(value = 1L) @Max(value = Long.MAX_VALUE)
        Long id;

        @Valid
        @JsonProperty(value = "amount")
        @NotNull
        @Min(value = 1) @Max(value = Integer.MAX_VALUE)
        Integer amount;
    }

    @NotNull
    private Geo geo;

    @NotNull
    @JsonProperty(value = "products")
    private List<ProductAmountPair> productAmountPairs;

}
