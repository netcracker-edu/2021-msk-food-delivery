package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.entities.UserRefreshToken;

import java.util.UUID;

public interface UserRefreshTokenService {

    public String createRefreshToken(User owner, String userAgent);
    public UserRefreshToken getTokenById(UUID token_id);
    public void deleteTokenById(UUID token_id);
    public long deleteTokensByOwner(User owner);
}
