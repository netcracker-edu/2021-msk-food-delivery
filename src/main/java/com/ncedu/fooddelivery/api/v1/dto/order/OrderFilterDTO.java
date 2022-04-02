package com.ncedu.fooddelivery.api.v1.dto.order;

import com.ncedu.fooddelivery.api.v1.entities.OrderStatus;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Data;
import org.hibernate.annotations.TypeDef;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderFilterDTO {

    @Min(value = 1) @Max(value = Long.MAX_VALUE)
    private Long clientId;

    @Min(value = 1) @Max(value = Long.MAX_VALUE)
    private Long warehouseId;

    @Min(value = 1) @Max(value = Long.MAX_VALUE) Long courierId;
    private String address;

    private OrderStatus status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateEnd;

    @Digits(integer = 5, fraction = 2) @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal overallCost;

    @Digits (integer = 1, fraction = 2) @DecimalMin("1.0") @DecimalMax("3.0")
    private BigDecimal highDemandCoeff;

    @Digits(integer = 5, fraction = 2) @DecimalMin(value = "0.0")
    private BigDecimal discount;

    @Min(value = 1) @Max(value = Long.MAX_VALUE)
    private Long promoCodeId;

    @Digits(integer = 1, fraction = 2) @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal clientRating;

    @Digits (integer = 1, fraction = 2) @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal deliveryRating;

}
