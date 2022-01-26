package com.ncedu.fooddelivery.api.v1.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "user_refresh_tokens")
@Data
public class UserRefreshToken {

    @Id
    @GeneratedValue
    @Column(name = "token_id")
    private UUID id;

    @Column(name = "create_date")
    private Timestamp createDate;

    @Column(name = "user_agent")
    private String userAgent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    public UserRefreshToken() {};
}
