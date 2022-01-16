package com.ncedu.fooddelivery.api.v1.entities.order;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ncedu.fooddelivery.api.v1.entities.OrderStatus;
import com.vividsolutions.jts.geom.Geometry;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@Entity
@Table(name = "orders")
public class OrderNotHierarchical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @NotNull
    @Column(name = "client_id")
    private Long clientId;

    @NotNull
    @Column(name = "address")
    private String address;

    @NotNull
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    @Column(name = "coordinates", columnDefinition = "geometry")
    private Geometry coordinates;

    @NotNull
    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "courier_id")
    private Long courierId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "order_status")
    @Column(name = "status")
    private OrderStatus status;     /* TODO: don't forget to check if status is null - change it for "CREATED"
                                       then */

    @NotNull
    @Column(name = "date_start")
    private Date dateStart;

    @Column(name = "date_end")
    private Date dateEnd;

    @NotNull
    @Column(name = "overall_cost")
    private Double overallCost;

    @NotNull
    @Column(name = "high_demand_coeff")
    private Double highDemandCoeff;      // TODO: similarly "1.0"

    @NotNull
    @Column(name = "discount")
    private Double discount;     // TODO: similarly "0.0"

    @Column(name = "promo_code_id")
    private Long promoCodeId;

    @Column(name = "client_rating")
    private BigDecimal clientRating;

    @Column(name = "delivery_rating")
    private BigDecimal deliveryRating;

    public OrderNotHierarchical(){}

    public OrderNotHierarchical(Long id, Long clientId, String address, Geometry coordinates, Long warehouseId, Long courierId, OrderStatus status, Date dateStart, Date dateEnd, Double overallCost, Double highDemandCoeff, Double discount, Long promoCodeId, BigDecimal clientRating, BigDecimal deliveryRating) {
        this.id = id;
        this.clientId = clientId;
        this.address = address;
        this.coordinates = coordinates;
        this.warehouseId = warehouseId;
        this.courierId = courierId;
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
