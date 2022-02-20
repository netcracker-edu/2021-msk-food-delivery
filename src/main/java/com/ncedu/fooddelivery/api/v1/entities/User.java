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
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
@TypeDef(
        name = "role_enum",
        typeClass = PostgreSQLEnumType.class
)
public class User implements Serializable, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user")
    @SequenceGenerator(name = "user", sequenceName = "users_user_id_seq", allocationSize = 1)
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

    public void setClient(Client client) {
        this.client = client;
        this.client.setUser(this);
    }

    @OneToOne(cascade=CascadeType.ALL, mappedBy = "user")
    @PrimaryKeyJoinColumn
    private Courier courier;
public void setCourier(Courier courier) {
       this.courier = courier;
       this.courier.setUser(this);
   }
    @OneToOne(cascade=CascadeType.ALL, mappedBy = "user")
    @PrimaryKeyJoinColumn
    private Moderator moderator;

    public void setModerator(Moderator moderator) {
        this.moderator = moderator;
        this.moderator.setUser(this);
    }

    @OneToOne(cascade=CascadeType.ALL, mappedBy = "user")
    @PrimaryKeyJoinColumn
    private Courier courier;

    public void setCourier(Courier courier) {
        this.courier = courier;
        this.courier.setUser(this);
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<File> files;

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
        return checkUserNotLocked();
    }

    @Override
    public boolean isAccountNonLocked() {
        return checkUserNotLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return checkUserNotLocked();
    }

    private boolean checkUserNotLocked() {
        return lockDate == null ? true : false;
    }
}
