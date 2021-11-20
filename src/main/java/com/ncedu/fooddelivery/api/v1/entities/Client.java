package com.ncedu.fooddelivery.api.v1.entities;

import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentData() {
        return paymentData;
    }

    public void setPaymentData(String paymentData) {
        this.paymentData = paymentData;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
