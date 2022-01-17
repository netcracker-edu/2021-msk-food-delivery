package com.ncedu.fooddelivery.api.v1.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CountOrderCostRequestDTO {

    @Data
    public static class Geo{

        @JsonProperty(value = "lat")
        @DecimalMin(value = "-90.0", inclusive = true)
        @DecimalMax(value = "90.0", inclusive = true)
        BigDecimal lat;

        @JsonProperty(value = "lon")
        @DecimalMin(value = "-180.0", inclusive = false)
        @DecimalMax(value = "180.0", inclusive = false)
        BigDecimal lon;

        public Geo(){

        }
    }

    @Data
    public static class ProductAmountPair{

        @JsonProperty(value = "productId")
        @NotNull
        @Min(value = 1L) @Max(value = Long.MAX_VALUE)
        Long id;

        @JsonProperty(value = "amount")
        @NotNull
        @Min(value = 1) @Max(value = Integer.MAX_VALUE)
        Integer amount;

        public ProductAmountPair() {
        }
    }

    @NotNull
    private Geo geo;

    @NotNull
    @JsonProperty(value = "products")
    private List<ProductAmountPair> productAmountPairs;

}
