package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.user.ModeratorInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Moderator;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.repos.ModeratorRepo;
import com.ncedu.fooddelivery.api.v1.services.ModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ModeratorServiceImpl implements ModeratorService {
    @Autowired
    ModeratorRepo moderatorRepo;

    public Moderator getModeratorById(Long id) {
        Optional<Moderator> moderator = moderatorRepo.findById(id);
        if (!moderator.isPresent()) {
            throw new NotFoundEx(id.toString());
        }
        return moderator.get();
    }

    public ModeratorInfoDTO getModeratorDTOById(Long id) {
        Moderator moderator = getModeratorById(id);
        return createModeratorDTO(moderator);
    }

    private ModeratorInfoDTO createModeratorDTO(Moderator moderator) {
        User user = moderator.getUser();
        return new ModeratorInfoDTO(user.getId(), user.getRole().name(),
                user.getFullName(), user.getEmail(),
                user.getLastSigninDate(), user.getAvatarId(),
                moderator.getWarehouseId());
    }
}
