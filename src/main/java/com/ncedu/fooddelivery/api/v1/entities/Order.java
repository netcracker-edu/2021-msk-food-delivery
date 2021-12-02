package com.ncedu.fooddelivery.api.v1.entities;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @NotNull
    @Column(name = "address")
    private String address;

    @NotNull
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    @Column(name = "coordinates", columnDefinition = "geometry")
    private String coordinates;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;     /* TODO: don't forget to check if status is null - change it for "CREATED"
                                       then */

    @NotNull
    @Column(name = "date_start")
    private Timestamp dateStart;

    @Column(name = "date_end")
    private Timestamp dateEnd;

    @NotNull
    @Column(name = "overall_cost")
    private Float overallCost;

    @NotNull
    @Column(name = "high_demand_coeff")
    private Float highDemandCoeff;      // TODO: similarly "1.0"

    @NotNull
    @Column(name = "discount")
    private Float discount;     // TODO: similarly "0.0"

    @Column(name = "promo_code_id")
    private Long promoCodeId;

    @Column(name = "client_rating")
    private Float clientRating;

    @Column(name = "delivery_rating")
    private Float deliveryRating;

    public Order(){}

    public Order(Long id, Client client, String address, String coordinates, Warehouse warehouse, Courier courier, OrderStatus status, Timestamp dateStart, Timestamp dateEnd, Float overallCost, Float highDemandCoeff, Float discount, Long promoCodeId, Float clientRating, Float deliveryRating) {
        this.id = id;
        this.client = client;
        this.address = address;
        this.coordinates = coordinates;
        this.warehouse = warehouse;
        this.courier = courier;
        this.status = status;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.overallCost = overallCost;
        this.highDemandCoeff = highDemandCoeff;
        this.discount = discount;
        this.promoCodeId = promoCodeId;
        this.clientRating = clientRating;
        this.deliveryRating = deliveryRating;
    }
}
