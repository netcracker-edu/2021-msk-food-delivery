package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.UserInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.User;

import java.util.List;

public interface UserService {

    public User getUserById(Long id);
    public UserInfoDTO getUserDTOById(Long id);

    public boolean deleteUserById(Long id);

    public List<UserInfoDTO> getAllAdmins();
    public List<UserInfoDTO> getAllUsers();
}
