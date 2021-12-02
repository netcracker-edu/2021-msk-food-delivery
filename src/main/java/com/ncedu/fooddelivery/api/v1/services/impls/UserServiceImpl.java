package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.user.EmailChangeDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.UserInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.repos.UserRepo;
import com.ncedu.fooddelivery.api.v1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder encoder;

    public User getUserById(Long id) {
        Optional<User> user = userRepo.findById(id);
        if (!user.isPresent()) {
            throw new NotFoundEx(id.toString());
        }
        return user.get();
    }

    public UserInfoDTO getUserDTOById(Long id) {
        User user = getUserById(id);
        return createUserDTO(user);
    }

    private UserInfoDTO createUserDTO(User user) {
       return new UserInfoDTO(user.getId(), user.getRole().name(),
                user.getFullName(), user.getEmail(),
                user.getLastSigninDate(), user.getAvatarId());
    }

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
    public boolean changeEmail(Long id, EmailChangeDTO newEmailInfo) {
        User user = getUserById(id);
        return changeEmail(user, newEmailInfo);
    }

    @Override
    public boolean changeEmail(User user, EmailChangeDTO newEmailInfo) {
        String newUserEmail = newEmailInfo.getEmail();
        User userWithNewEmail = userRepo.findByEmail(newUserEmail);
        //user with new email also exist throw exception!
        if (userWithNewEmail != null) {
            return false; //TODO create special exception and throw it
        }
        String inputPassword = newEmailInfo.getPassword();
        String userEncodedPassword = user.getPassword();
        boolean isPasswordsSame = encoder.matches(inputPassword, userEncodedPassword);
        if (!isPasswordsSame) {
            return false; //TODO throw special exception
        }
        user.setEmail(newUserEmail);
        userRepo.save(user);
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
}
