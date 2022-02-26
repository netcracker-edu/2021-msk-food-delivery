package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.user.EmailChangeDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.PasswordChangeDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.UserInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.File;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.AlreadyExistsException;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.PasswordsMismatchException;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.repos.UserRepo;
import com.ncedu.fooddelivery.api.v1.services.FileService;
import com.ncedu.fooddelivery.api.v1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    FileService fileService;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public User getUserById(Long id) {
        Optional<User> user = userRepo.findById(id);
        if (!user.isPresent()) {
            throw new NotFoundEx(id.toString());
        }
        return user.get();
    }

    @Override
    public UserInfoDTO getUserDTOById(Long id) {
        User user = getUserById(id);
        return createUserDTO(user);
    }

    private UserInfoDTO createUserDTO(User user) {
       return new UserInfoDTO(user.getId(), user.getRole().name(),
                user.getFullName(), user.getEmail(),
                user.getLastSigninDate(), user.getAvatarId(), user.getLockDate());
    }

    @Override
    public boolean deleteUserById(Long id) {
        User userForDelete = getUserById(id);
        userRepo.delete(userForDelete);
        return true;
    }

    @Override
    public boolean changeFullName(Long id, String newFullName) {
        User user = getUserById(id);
        if (newFullName != null) {
            user.setFullName(newFullName);
            userRepo.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean changeEmail(User user, EmailChangeDTO newEmailInfo) {
        String newUserEmail = newEmailInfo.getEmail();
        User userWithNewEmail = userRepo.findByEmail(newUserEmail);
        //user with new email also exist throw exception!
        if (userWithNewEmail != null) {
            throw new AlreadyExistsException(newUserEmail);
        }
        String inputPassword = newEmailInfo.getPassword();
        String userEncodedPassword = user.getPassword();
        boolean isPasswordsSame = encoder.matches(inputPassword, userEncodedPassword);
        if (!isPasswordsSame) {
           throw new PasswordsMismatchException();
        }
        user.setEmail(newUserEmail);
        userRepo.save(user);
        return true;
    }

    @Override
    public boolean changePassword(User authedUser, PasswordChangeDTO passwordChangeDTO) {
        String inputOldPassword = passwordChangeDTO.getOldPassword();
        String userEncodedPassword = authedUser.getPassword();
        boolean isPasswordsMismatch = !encoder.matches(inputOldPassword, userEncodedPassword);
        if (isPasswordsMismatch) {
            throw  new PasswordsMismatchException();
        }
        String newPassword = passwordChangeDTO.getNewPassword();
        String newPasswordConfirm = passwordChangeDTO.getNewPasswordConfirm();
        isPasswordsMismatch = !newPassword.equals(newPasswordConfirm);
        if (isPasswordsMismatch) {
            throw  new PasswordsMismatchException();
        }

        authedUser.setPassword(encoder.encode(newPassword));
        userRepo.save(authedUser);
        return true;
    }

    public List<UserInfoDTO> getAllAdmins() {
        Role adminRole = Role.ADMIN;
        List<User> admins = userRepo.findByRole(adminRole);
        List<UserInfoDTO> adminsDTO = new ArrayList<>();
        for (User admin : admins) {
            adminsDTO.add(createUserDTO(admin));
        }
        return adminsDTO;
    }

    public List<UserInfoDTO> getAllUsers(Pageable pageable) {
        Iterable<User> users = userRepo.findAll(pageable);
        Iterator<User> iterator = users.iterator();

        List<UserInfoDTO> usersDTO = new ArrayList<>();
        while (iterator.hasNext()) {
            usersDTO.add(createUserDTO(iterator.next()));
        }
        return usersDTO;
    }

    @Override
    public void setLastSigninFromNow(User user) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        user.setLastSigninDate(now);
        userRepo.save(user);
    }

    @Override
    public UserInfoDTO lockUser(User user) {
        if (user.getLockDate() != null) {
            return createUserDTO(user);
        }
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        user.setLockDate(now);
        userRepo.save(user);
        return createUserDTO(user);
    }

    @Override
    public UserInfoDTO unlockUser(User user) {
        if (user.getLockDate() == null) {
            return createUserDTO(user);
        }
        user.setLockDate(null);
        userRepo.save(user);
        return createUserDTO(user);
    }

    @Override
    public List<UserInfoDTO> searchUsers(String phrase, Pageable pageable) {
        String resultPhrase = preparePhraseToSearch(phrase);
        Iterable<User> users = userRepo.searchUsers(resultPhrase, pageable);
        Iterator<User> iterator = users.iterator();
        List<UserInfoDTO> usersDTO = new ArrayList<>();
        while (iterator.hasNext()) {
            usersDTO.add(createUserDTO(iterator.next()));
        }
        return usersDTO;
    }

    private String preparePhraseToSearch(String phrase) {
        String[] splitedPhrase = phrase.split(" ");
        if (splitedPhrase.length == 1) {
            return phrase + ":*";
        }
        for (int i = 0; i < splitedPhrase.length; i++) {
            splitedPhrase[i] += ":*";
        }
        return String.join(" & ", splitedPhrase);
    }

    @Override
    public UserInfoDTO addAvatar(User authedUser, String fileUuid) {
        UUID avatarId = UUID.fromString(fileUuid);
        File avatar = fileService.getFile(avatarId);
        authedUser.setAvatarId(avatar.getId());
        userRepo.save(authedUser);
        return createUserDTO(authedUser);
    }

    @Override
    public void deleteAvatar(User user) {
        UUID avatarId = user.getAvatarId();
        if (avatarId == null) {
            return;
        }
        user.setAvatarId(null);
        userRepo.save(user);
        File avatar = fileService.getFile(avatarId);
        if (avatar != null) {
            fileService.delete(avatar, user);
        }
    }

    @Override
    public void deleteAvatar(User targetUser, User authedUser) {
        UUID avatarId = targetUser.getAvatarId();
        if (avatarId == null) {
            return;
        }
        targetUser.setAvatarId(null);
        userRepo.save(targetUser);
        File avatar = fileService.getFile(avatarId);
        if (avatar != null) {
            fileService.delete(avatar, authedUser);
        }
    }
}
