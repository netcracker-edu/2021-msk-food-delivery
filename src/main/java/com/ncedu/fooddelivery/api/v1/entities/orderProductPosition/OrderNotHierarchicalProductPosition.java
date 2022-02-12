package com.ncedu.fooddelivery.api.v1.entities.orderProductPosition;

import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPosition;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "orders_product_positions")
public class OrderNotHierarchicalProductPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_position_id")
    private Long id;

    @NotNull
    @Column(name = "order_id")
    private Long orderId;

    @NotNull
    @JoinColumn(name = "product_position_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ProductPosition productPosition;

    @NotNull
    @Column(name = "amount")
    private Integer amount;

    @NotNull
    @Column(name = "price")
    private Float price;

    public OrderNotHierarchicalProductPosition(){}

    public OrderNotHierarchicalProductPosition(Long id, Long orderId, ProductPosition productPosition, Integer amount, Float price) {
        this.id = id;
        this.orderId = orderId;
        this.productPosition = productPosition;
        this.amount = amount;
        this.price = price;
    }
}
