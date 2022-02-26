package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.user.EmailChangeDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.PasswordChangeDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.UserInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    public User getUserById(Long id);
    public UserInfoDTO getUserDTOById(Long id);

    public boolean deleteUserById(Long id);

    public boolean changeFullName(Long authedUserId, String newFullName);
    public boolean changeEmail(User user, EmailChangeDTO newEmailInfo);
    boolean changePassword(User authedUser, PasswordChangeDTO passwordChangeDTO);

    public List<UserInfoDTO> getAllAdmins();
    public List<UserInfoDTO> getAllUsers(Pageable pageable);

    public void setLastSigninFromNow(User user);

    public UserInfoDTO lockUser(User user);
    public UserInfoDTO unlockUser(User user);
    public List<UserInfoDTO> searchUsers(String phrase, Pageable pageable);
    public UserInfoDTO addAvatar(User authedUser, String fileUuid);
    public void deleteAvatar(User user);
    public void deleteAvatar(User targetUser, User authedUser);
}
