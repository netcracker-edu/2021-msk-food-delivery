package com.ncedu.fooddelivery.api.v1.entities;


import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
@TypeDef(
        name = "role_enum",
        typeClass = PostgreSQLEnumType.class
)
public class User implements Serializable, UserDetails {
    //TODO: problems with sequences when add to DB. We populated DB from data.sql, but hibernate_sequence whatever start from 1
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Type( type = "role_enum" )
    private Role role;
    private String password;
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

    @OneToOne(cascade=CascadeType.ALL, mappedBy = "user")
    @PrimaryKeyJoinColumn
    private Client client;

    @OneToOne(cascade=CascadeType.ALL, mappedBy = "user")
    @PrimaryKeyJoinColumn
    private Moderator moderator;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (lockDate == null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (lockDate == null) {
            return true;
        }
        return false;
    }
}