package com.ncedu.fooddelivery.api.v1.entities.orderProductPosition;

import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPosition;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "orders_product_positions")
public class OrderProductPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_position_id")
    private Long id;

    @NotNull
    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Order order;

    @NotNull
    @JoinColumn(name = "product_position_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ProductPosition productPosition;

    @NotNull
    @Column(name = "amount")
    private Integer amount;

    @NotNull
    @Column(name = "price")
    private Double price;

    public OrderProductPosition(){}

    public OrderProductPosition(Order order, ProductPosition productPosition, Integer amount, Double price) {
        this.order = order;
        this.productPosition = productPosition;
        this.amount = amount;
        this.price = price;
    }

    public OrderProductPosition(Long id, Order order, ProductPosition productPosition, Integer amount, Double price) {
        this.id = id;
        this.order = order;
        this.productPosition = productPosition;
        this.amount = amount;
        this.price = price;
    }
}
