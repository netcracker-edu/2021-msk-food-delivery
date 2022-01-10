package com.ncedu.fooddelivery.api.v1.dto;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ncedu.fooddelivery.api.v1.dto.user.UserInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.*;
import com.vividsolutions.jts.geom.Geometry;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

    private OrderStatus status;     /* TODO: don't forget to check if status is null - change it for "CREATED"
                                       then */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateStart;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateEnd;

    private BigDecimal overallCost;

    private BigDecimal highDemandCoeff;      // TODO: similarly "1.0"

    private BigDecimal discount;     // TODO: similarly "0.0"

    private Long promoCodeId;

    private BigDecimal clientRating;

    private BigDecimal deliveryRating;

    public OrderInfoDTO(Long id, Client client, String address, Geometry coordinates,
                        Warehouse warehouse, Courier courier, OrderStatus status, LocalDateTime dateStart,
                        LocalDateTime dateEnd, BigDecimal overallCost, BigDecimal highDemandCoeff,
                        BigDecimal discount, Long promoCodeId, BigDecimal clientRating, BigDecimal deliveryRating) {
        this.id = id;
        User userClient = client.getUser();
        this.client = new ClientInfoDTO(
                client.getId(), userClient.getRole().toString(), userClient.getFullName(), userClient.getEmail(),
                userClient.getLastSigninDate(), userClient.getAvatarId(), client.getPhoneNumber(),
                client.getRating());
        this.address = address;
        this.coordinates = coordinates;
        this.warehouse = warehouse;
        User userCourier = courier.getUser();
        this.courier = new CourierInfoDTO(userCourier.getId(), userCourier.getRole().toString(), userCourier.getFullName(),
                userCourier.getEmail(), userCourier.getLastSigninDate(), userCourier.getAvatarId(),
                courier.getPhoneNumber(), courier.getRating(), courier.getWarehouse().getId(),
                courier.getAddress(), courier.getCurrentBalance());
        this.status = status;
        this.dateStart = dateStart.atZone(ZoneOffset.ofHours(3)).toLocalDateTime();
        this.dateEnd = dateEnd.atZone(ZoneOffset.ofHours(3)).toLocalDateTime();
        this.overallCost = overallCost;
        this.highDemandCoeff = highDemandCoeff;
        this.discount = discount;
        this.promoCodeId = promoCodeId;
        this.clientRating = clientRating;
        this.deliveryRating = deliveryRating;
    }
}
