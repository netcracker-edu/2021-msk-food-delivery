package com.ncedu.fooddelivery.api.v1.entities.order;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ncedu.fooddelivery.api.v1.entities.Client;
import com.ncedu.fooddelivery.api.v1.entities.Courier;
import com.ncedu.fooddelivery.api.v1.entities.OrderStatus;
import com.ncedu.fooddelivery.api.v1.entities.Warehouse;
import com.vividsolutions.jts.geom.Geometry;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

@TypeDef(
        name = "order_status",
        typeClass = PostgreSQLEnumType.class
)

@Data
@AllArgsConstructor
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
    private Geometry coordinates;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "order_status")
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

    public Order(){}
}
