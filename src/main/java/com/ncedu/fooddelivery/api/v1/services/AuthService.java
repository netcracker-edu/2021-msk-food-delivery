package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;
    //TODO: add client and courier repos for finding user by phoneNumber

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(login);

        if (user == null) {
            throw new UsernameNotFoundException("Can't find user with this login: " + login);
        }
        UserDetails userDetails = createUser(user);

        return userDetails;
    }

    private UserDetails createUser(User user) {
        return new org.springframework.security.core.userdetails.User
                (user.getEmail(), user.getPassword(), createAuthorities(user));
    }

    private Collection<GrantedAuthority> createAuthorities(User user) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        return  authorities;
    }
}
