package com.ncedu.fooddelivery.api.v1.entities;

import lombok.Data;
import javax.persistence.*;

@Data
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
}
