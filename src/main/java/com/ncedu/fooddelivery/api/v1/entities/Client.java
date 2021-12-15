package com.ncedu.fooddelivery.api.v1.entities;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "clients")
@TypeDef(
        name = "json",
        typeClass = JsonType.class
)
public class Client implements Serializable {

    @Id
    @Column(name = "client_id")
    private Long id;

    @Column(name = "payment_data")
    @Type(type = "json")
    private String paymentData;

    @Column(name = "phone_number")
    private String phoneNumber;

    private Double rating;

    @MapsId
    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "user_id")
    private User user;
}