package com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ncedu.fooddelivery.api.v1.entities.Product;
import com.ncedu.fooddelivery.api.v1.entities.Warehouse;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductPositionInfoDTO {
    private Long id;
    private Product product;
    private Warehouse warehouse;
    private String warehouseSection;
    private Integer supplyAmount;
    private Integer currentAmount;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date supplyDate;

    private BigDecimal supplierInvoice;
    private String supplierName;
    private Boolean isInvoicePaid;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date manufactureDate;



    public ProductPositionInfoDTO(){}

    public ProductPositionInfoDTO(Long id, Product product, Warehouse warehouse, String warehouseSection, Integer supplyAmount, Integer currentAmount, Date supplyDate, BigDecimal supplierInvoice, String supplierName, Boolean isInvoicePaid, Date manufactureDate) {
        this.id = id;
        this.product = product;
        this.warehouse = warehouse;
        this.warehouseSection = warehouseSection;
        this.supplyAmount = supplyAmount;
        this.currentAmount = currentAmount;
        this.supplyDate = supplyDate;
        this.supplierInvoice = supplierInvoice;
        this.supplierName = supplierName;
        this.isInvoicePaid = isInvoicePaid;
        this.manufactureDate = manufactureDate;
    }

}
