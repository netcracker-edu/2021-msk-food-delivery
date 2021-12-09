package com.ncedu.fooddelivery.api.v1.mappers;

import com.ncedu.fooddelivery.api.v1.dto.user.RegistrationDTO;
import com.ncedu.fooddelivery.api.v1.entities.Client;
import com.ncedu.fooddelivery.api.v1.entities.Moderator;
import com.ncedu.fooddelivery.api.v1.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RegistrationMapper {

    public RegistrationMapper INSTANCE = Mappers.getMapper(RegistrationMapper.class);

    public User dtoToUser(RegistrationDTO registrationDTO);
    public Client dtoToClient(RegistrationDTO registrationDTO);
    public Moderator dtoToModerator(RegistrationDTO registrationDTO);
}
