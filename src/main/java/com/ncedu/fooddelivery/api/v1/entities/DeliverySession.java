package com.ncedu.fooddelivery.api.v1.entities;

import com.vladmihalcea.hibernate.type.interval.PostgreSQLIntervalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_sessions")
@Data
@AllArgsConstructor
@TypeDef(
        typeClass = PostgreSQLIntervalType.class,
        defaultForType = Duration.class
)
public class DeliverySession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_session_id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @NotNull
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "orders_completed")
    @Min(value = 0)
    private Integer ordersCompleted;

    @Column(
            name = "average_time_per_order",
            columnDefinition = "interval"
    )
    private Duration averageTimePerOrder;

    @Column(name = "money_earned")
    private Float moneyEarned;

    public DeliverySession(){}
}
