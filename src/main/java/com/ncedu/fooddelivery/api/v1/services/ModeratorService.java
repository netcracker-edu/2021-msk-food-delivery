package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.ModeratorInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Moderator;

public interface ModeratorService {

    public Moderator getModeratorById(Long id);
    public ModeratorInfoDTO getModeratorDTOById(Long id);
}
