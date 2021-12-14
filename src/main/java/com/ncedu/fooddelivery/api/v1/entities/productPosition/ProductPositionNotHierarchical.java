package com.ncedu.fooddelivery.api.v1.entities.productPosition;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;
import java.math.BigDecimal;

@Data
@Entity
@Component
@Table(name = "product_positions")
public class ProductPositionNotHierarchical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_position_id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "warehouse_section")
    private String warehouseSection;

    @Column(name = "supply_amount")
    private Integer supplyAmount;

    @Column(name = "current_amount")
    private Integer currentAmount;

    @Column(name = "supply_date")
    private Date supplyDate;

    @Column(name = "supplier_invoice")
    private BigDecimal supplierInvoice;

    @Column(name = "supplier_name")
    private String supplierName;

    @Column(name = "is_invoice_paid")
    private Boolean isInvoicePaid;

    @Column(name = "manufacture_date")
    private Date manufactureDate;

    public ProductPositionNotHierarchical(Long id, Long productId, Long warehouseId, String warehouseSection, Integer supplyAmount, Integer currentAmount, Date supplyDate, BigDecimal supplierInvoice, String supplierName, Boolean isInvoicePaid, Date manufactureDate) {
        this.id = id;
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.warehouseSection = warehouseSection;
        this.supplyAmount = supplyAmount;
        this.currentAmount = currentAmount;
        this.supplyDate = supplyDate;
        this.supplierInvoice = supplierInvoice;
        this.supplierName = supplierName;
        this.isInvoicePaid = isInvoicePaid;
        this.manufactureDate = manufactureDate;
    }

    public ProductPositionNotHierarchical(){}
}