package com.ncedu.fooddelivery.api.v1.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "couriers")
public class Courier {

    @Id
    @Column(name = "courier_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "payment_data")
    @Type(type = "json")
    private String paymentData;

    @NotNull
    @Column(name = "address")
    private String address;

    @NotNull
    @Column(name = "current_balance")
    private Float currentBalance;

    @Column(name = "rating")
    private Float rating;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "courier_id")

    @JsonIgnoreProperties("courier")
    private User user;

    public Courier(){}

    public Courier(Long id, Warehouse warehouse, String phoneNumber, String paymentData, String address, Float currentBalance, Float rating) {
        this.id = id;
        this.warehouse = warehouse;
        this.phoneNumber = phoneNumber;
        this.paymentData = paymentData;
        this.address = address;
        this.currentBalance = currentBalance;
        this.rating = rating;
    }
}
