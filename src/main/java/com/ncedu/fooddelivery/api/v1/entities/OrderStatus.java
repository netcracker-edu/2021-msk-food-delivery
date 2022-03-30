package com.ncedu.fooddelivery.api.v1.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    CREATED("CREATED"),
    COURIER_APPOINTED("COURIER_APPOINTED"),
    PACKING("PACKING"),
    DELIVERING("DELIVERING"),
    DELIVERED("DELIVERED"),
    CANCELLED("CANCELLED");

    private String status;

    public String getStatus() {
        return this.status;
    }

    OrderStatus(String status) {
        this.status = status;
    }
}
