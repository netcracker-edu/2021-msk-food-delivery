package com.ncedu.fooddelivery.api.v1.dto.deliverySession;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ncedu.fooddelivery.api.v1.entities.Courier;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
public class DeliverySessionInfoDTO {
    private Long id;

    private Courier courier;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private Integer ordersCompleted;

    private String averageTimePerOrder;

    private Float moneyEarned;

    public DeliverySessionInfoDTO(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliverySessionInfoDTO that = (DeliverySessionInfoDTO) o;
        return id.equals(that.id) && courier.getId().equals(that.courier.getId()) && startTime.equals(that.startTime)
                && Objects.equals(endTime, that.endTime) && Objects.equals(ordersCompleted, that.ordersCompleted)
                && Objects.equals(averageTimePerOrder, that.averageTimePerOrder)
                && Objects.equals(moneyEarned, that.moneyEarned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courier.getId(), startTime, endTime, ordersCompleted, averageTimePerOrder, moneyEarned);
    }
}
