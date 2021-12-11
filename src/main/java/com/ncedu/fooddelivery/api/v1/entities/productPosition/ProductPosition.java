package com.ncedu.fooddelivery.api.v1.entities.productPosition;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ncedu.fooddelivery.api.v1.entities.Product;
import com.ncedu.fooddelivery.api.v1.entities.Warehouse;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;
import java.math.BigDecimal;

@Data
@Entity
@Component
@Table(name = "product_positions")
public class ProductPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_position_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "warehouse_section")
    private String warehouseSection;

    @Column(name = "supply_amount")
    private Integer supplyAmount;

    @Column(name = "current_amount")
    private Integer currentAmount;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "supply_date")
    private Date supplyDate;

    @Column(name = "supplier_invoice")
    private BigDecimal supplierInvoice;

    @Column(name = "supplier_name")
    private String supplierName;

    @Column(name = "is_invoice_paid")
    private Boolean isInvoicePaid;

    @Column(name = "manufacture_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date manufactureDate;

    public ProductPosition(Long id, Product product, Warehouse warehouse, String warehouseSection, Integer supplyAmount, Integer currentAmount, Date supplyDate, BigDecimal supplierInvoice, String supplierName, Boolean isInvoicePaid, Date manufactureDate) {
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

    public ProductPosition(){}
}
