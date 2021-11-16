package com.ncedu.fooddelivery.api.v1.entities;

import javax.persistence.*;

@Entity
@Table(name = "moderators")
public class Moderator {
    @Id
    @Column(name = "moderator_id")
    private Long id;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "moderatorId", referencedColumnName = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
