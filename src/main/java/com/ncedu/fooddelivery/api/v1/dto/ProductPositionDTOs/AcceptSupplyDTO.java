package com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class AcceptSupplyDTO {

    @NotNull
    private Long productId;
    @NotNull
    private Long warehouseId;

    @Min(value = 1)
    @Max(value = Integer.MAX_VALUE)
    private Integer supplyAmount;

    @NotEmpty
    @Pattern(regexp = "[А-Я]-[0-9]-[0-9][0-9][0-9]-[А-Я]")
    private String warehouseSection;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=12, fraction=2)
    private BigDecimal supplierInvoice;

    @NotEmpty
    private String supplierName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @PastOrPresent
    private Date manufactureDate;

    private Boolean isInvoicePaid;
}
