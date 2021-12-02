package com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class UpdatePaymentStatusDTO {
    @NotEmpty
    private List<@NotNull @Min(value = 1) @Max(value = Long.MAX_VALUE) Long> productPositions;
}
