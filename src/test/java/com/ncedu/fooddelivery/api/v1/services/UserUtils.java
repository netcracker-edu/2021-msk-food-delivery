package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;

public class UserUtils {

    public static User moderatorLeonardHofstadter(Long id) {
        final String fullName = "Leonard Leakey Hofstadter";
        final String email = "hofstadter@bigbang.theory";
        final Role role = Role.MODERATOR;
        return  createUser(id, fullName, email, role);
    }

    public static User clientPennyTeller(Long id) {
        final String fullName = "Penny Teller";
        final String email = "penny@bigbang.theory";
        final Role role = Role.CLIENT;
        return  createUser(id, fullName, email, role);
    }
    public static User adminSheldonCooper(Long id) {
        final String fullName = "Sheldon Lee Cooper";
        final String email = "cooper@bigbang.theory";
        final Role role = Role.ADMIN;
        return  createUser(id, fullName, email, role);
    }

    public static User courierHowardWolowitz(Long id) {
        final String fullName = "Howard Joel Wolowitz";
        final String email = "wolowitz@bigbang.theory";
        final Role role = Role.COURIER;
        return  createUser(id, fullName, email, role);

    }
    public static User clientRajeshKoothrappali (Long id) {
        final String fullName = "Rajesh Ramayan Koothrappali";
        final String email = "rajesh@bigbang.theory";
        final Role role = Role.CLIENT;
        return  createUser(id, fullName, email, role);
    }

    public static User createUser(Long id, String fullName, String email, Role role) {
        User user = new User();
        user.setId(id);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setRole(role);
        return user;
    }

}
