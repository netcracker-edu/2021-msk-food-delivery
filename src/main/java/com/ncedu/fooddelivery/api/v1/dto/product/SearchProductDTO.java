package com.ncedu.fooddelivery.api.v1.dto.product;

import com.ncedu.fooddelivery.api.v1.dto.CoordsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class SearchProductDTO {

    @NotBlank
    private String phrase;
    @NotNull
    private CoordsDTO geo;

    SearchProductDTO() {};
}
