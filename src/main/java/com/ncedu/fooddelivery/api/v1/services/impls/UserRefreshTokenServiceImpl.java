package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.entities.UserRefreshToken;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.repos.UserRefreshTokenRepo;
import com.ncedu.fooddelivery.api.v1.services.UserRefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserRefreshTokenServiceImpl implements UserRefreshTokenService {

    @Autowired
    UserRefreshTokenRepo userRefreshTokenRepo;

    @Override
    public String createRefreshToken(User owner, String userAgent) {
        UserRefreshToken urt = new UserRefreshToken();
        Timestamp createDate = Timestamp.valueOf(LocalDateTime.now());
        urt.setCreateDate(createDate);
        urt.setOwner(owner);
        urt.setUserAgent(userAgent);
        urt = userRefreshTokenRepo.save(urt);
        return urt.getId().toString();
    }

    @Override
    public UserRefreshToken getTokenById(UUID token_id) {
        Optional<UserRefreshToken> urt = userRefreshTokenRepo.findById(token_id);
        if (urt.isPresent()) {
            return urt.get();
        }
        throw new NotFoundEx(token_id.toString());
    }

    @Override
    public void deleteToken(UserRefreshToken userRefreshToken) {
       userRefreshTokenRepo.delete(userRefreshToken);
    }

    @Override
    public long deleteTokensByOwner(User owner) {
        long deletedTokensCount = userRefreshTokenRepo.deleteByOwner(owner);
        return deletedTokensCount;
    }
}
