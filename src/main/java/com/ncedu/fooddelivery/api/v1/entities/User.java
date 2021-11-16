package com.ncedu.fooddelivery.api.v1.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;
    private byte[] password;
    @Column(name = "full_name")
    private String fullName;
    private String email;
    @Column(name = "reg_date")
    private Timestamp regDate;
    @Column(name = "last_signin_date")
    private Timestamp lastSigninDate;
    @Column(name = "avatar_id")
    private UUID avatarId;
    @Column(name = "lock_date")
    private Timestamp lockDate;

    @OneToOne(mappedBy = "user")
    private Client client;


    User() {

    }

    public User(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getRegDate() {
        return regDate;
    }

    public void setRegDate(Timestamp regDate) {
        this.regDate = regDate;
    }

    public Timestamp getLastSigninDate() {
        return lastSigninDate;
    }

    public void setLastSigninDate(Timestamp lastSigninDate) {
        this.lastSigninDate = lastSigninDate;
    }

    public UUID getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(UUID avatarId) {
        this.avatarId = avatarId;
    }

    public Timestamp getLockDate() {
        return lockDate;
    }

    public void setLockDate(Timestamp lockDate) {
        this.lockDate = lockDate;
    }
}