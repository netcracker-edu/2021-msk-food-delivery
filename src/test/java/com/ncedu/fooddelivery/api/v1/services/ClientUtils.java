package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.user.ClientInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Client;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;

public class ClientUtils {

    public static Client createLeonardHofstadter(Long userId) {
        final String fullName = "Leonard Leakey Hofstadter";
        final String email = "hofstadter@bigbang.theory";
        final String phoneNumber = "+7 (800) 555 35-35";
        return createClient(userId, fullName, email, phoneNumber);
    }

    public static Client createPennyTeller(Long userID) {
        final String fullName = "Penny Teller";
        final String email = "penny@bigbang.theory";
        final String phoneNumber = "+7 (900) 555 35-35";
        return createClient(userID, fullName, email, phoneNumber);
    }

    private static Client createClient(Long userId, String fullName, String email, String phoneNumber) {
        User user = new User();
        user.setId(userId);
        user.setFullName(fullName);
        user.setEmail("hofstadter@bigbang.theory");
        user.setRole(Role.CLIENT);
        Client client = new Client();
        client.setPhoneNumber("+7 (800) 555 35-35");
        client.setUser(user);
        return client;
    }

    public static ClientInfoDTO createClientInfoDTO(Client client) {
        User user = client.getUser();
        return  new ClientInfoDTO(user.getId(), user.getRole().name(),
                user.getFullName(), user.getEmail(),
                user.getLastSigninDate(), user.getAvatarId(),
                client.getPhoneNumber(), client.getRating());
    }
}
