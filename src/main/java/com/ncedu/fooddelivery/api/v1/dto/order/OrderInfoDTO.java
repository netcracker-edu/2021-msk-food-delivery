package com.ncedu.fooddelivery.api.v1.dto.order;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ncedu.fooddelivery.api.v1.dto.user.ClientInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.CourierInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.UserInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.*;
import com.ncedu.fooddelivery.api.v1.entities.orderProductPosition.OrderNotHierarchicalProductPosition;
import com.vividsolutions.jts.geom.Geometry;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@TypeDef(
        name = "order_status",
        typeClass = PostgreSQLEnumType.class
)

@Data
public class OrderInfoDTO {
    private Long id;

    @JsonProperty(value = "client")
    private UserInfoDTO client;

    private String address;

    @JsonSerialize(using = GeometrySerializer.class)
    private Geometry coordinates;

    @JsonProperty(value = "warehouse")
    private Warehouse warehouse;

    @JsonProperty(value = "courier")
    private UserInfoDTO courier;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "order_status")
    private OrderStatus status;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateStart;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateEnd;

    private Double overallCost;

    private Double highDemandCoeff;

    private Double discount;

    private Long promoCodeId;

    private BigDecimal clientRating;

    private BigDecimal deliveryRating;

    private List<OrderNotHierarchicalProductPosition> productPositions;

    public OrderInfoDTO(Long id, Client client, String address, Geometry coordinates,
                        Warehouse warehouse, Courier courier, OrderStatus status, LocalDateTime dateStart,
                        LocalDateTime dateEnd, Double overallCost, Double highDemandCoeff,
                        Double discount, Long promoCodeId, BigDecimal clientRating, BigDecimal deliveryRating, List<OrderNotHierarchicalProductPosition> productPositions) {
        this.id = id;
        User userClient = client.getUser();
        this.client = new ClientInfoDTO(
                client.getId(), userClient.getRole().toString(), userClient.getFullName(), userClient.getEmail(),
                userClient.getLastSigninDate(), userClient.getAvatarId(), client.getPhoneNumber(),
                client.getRating());
        this.address = address;
        this.coordinates = coordinates;
        this.warehouse = warehouse;

        if(courier == null) this.courier = null;
        else {
            User userCourier = courier.getUser();
            this.courier = new CourierInfoDTO(userCourier.getId(), userCourier.getRole().toString(), userCourier.getFullName(),
                    userCourier.getEmail(), userCourier.getLastSigninDate(), userCourier.getAvatarId(),
                    courier.getPhoneNumber(), courier.getRating(), courier.getWarehouse() != null ?
                    courier.getWarehouse().getId() : null,
                    courier.getAddress(), courier.getCurrentBalance());
        }
        this.status = status;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.overallCost = overallCost;
        this.highDemandCoeff = highDemandCoeff;
        this.discount = discount;
        this.promoCodeId = promoCodeId;
        this.clientRating = clientRating;
        this.deliveryRating = deliveryRating;
        this.productPositions = productPositions;
    }
}
