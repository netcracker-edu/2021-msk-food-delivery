package com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
public class ProductPositionFilterDTO {

    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    private Long warehouseId;
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    private Long productId;
    private String warehouseSection;
    @Min(value = 1)
    @Max(value = Integer.MAX_VALUE)
    private Integer supplyAmount;
    @Min(value = 1)
    @Max(value = Integer.MAX_VALUE)
    private Integer currentAmount;
    @Digits(integer = 10, fraction = 2)
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal supplierInvoice;
    private String supplierName;
    private Boolean isInvoicePaid;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date supplyDate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date manufactureDate;

    public ProductPositionFilterDTO() {};
}
