package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.user.EmailChangeDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.PasswordChangeDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.UserInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.AlreadyExistsException;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.PasswordsMismatchException;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.repos.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
public class UserServiceTest {

    @MockBean
    UserRepo userRepoMock;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void getUserByIdSuccess() {
        Long userId = 1L;
        User sheldonCooper = UserUtils.adminSheldonCooper(userId);

        when(userRepoMock.findById(userId)).thenReturn(Optional.of(sheldonCooper));

        User result = userService.getUserById(userId);
        assertEquals(sheldonCooper, result);
        verify(userRepoMock, times(1)).findById(userId);
    }

    @Test
    public void getUserByIdNotFound() {
        Long userId = 0L;
        when(userRepoMock.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundEx.class, () -> {
            userService.getUserById(userId);
        });
        String resultMessage = exception.getMessage();
        String perfectMessage = new NotFoundEx(userId.toString()).getMessage();
        assertEquals(perfectMessage, resultMessage);
        verify(userRepoMock, times(1)).findById(userId);
    }

    @Test
    public void getUserDTOByIdSuccess() {
        Long userId = 1L;
        User sheldonCooper = UserUtils.adminSheldonCooper(userId);

        when(userRepoMock.findById(userId)).thenReturn(Optional.of(sheldonCooper));

        UserInfoDTO resultDTO = userService.getUserDTOById(userId);
        UserInfoDTO perfectDTO = UserUtils.createUserDTO(sheldonCooper);
        assertEquals(perfectDTO, resultDTO);
        verify(userRepoMock, times(1)).findById(userId);
    }

    @Test
    public void getUserDTOByIdNotFound() {
        Long userId = 0L;
        when(userRepoMock.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundEx.class, () -> {
            userService.getUserDTOById(userId);
        });
        String resultMessage = exception.getMessage();
        String perfectMessage = new NotFoundEx(userId.toString()).getMessage();
        assertEquals(perfectMessage, resultMessage);
        verify(userRepoMock, times(1)).findById(userId);
    }

    @Test
    public void deleteUserByIdNotFound() {
        Long userId = 0L;
        when(userRepoMock.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundEx.class, () -> {
            userService.deleteUserById(userId);
        });
        String resultMessage = exception.getMessage();
        String perfectMessage = new NotFoundEx(userId.toString()).getMessage();
        assertEquals(perfectMessage, resultMessage);
        verify(userRepoMock, times(1)).findById(userId);
        verify(userRepoMock, never()).delete(any());
    }

    @Test
    public void deleteUserByIdSuccess() {
        Long userId = 1L;
        User rajesh = UserUtils.clientRajeshKoothrappali(userId);
        when(userRepoMock.findById(userId)).thenReturn(Optional.of(rajesh));
        doNothing().when(userRepoMock).delete(rajesh);

        boolean result = userService.deleteUserById(userId);
        assertTrue(result);
        verify(userRepoMock, times(1)).findById(userId);
        verify(userRepoMock, times(1)).delete(rajesh);
    }

    @Test
    public void changeFullNameSuccess() {
        Long userId = 1L;
        User penny = UserUtils.clientPennyTeller(userId);
        String newFullName = "Penny Hofstadter";
        User pennyWithNewFullName = UserUtils.clientPennyTeller(userId);
        pennyWithNewFullName.setFullName(newFullName);

        when(userRepoMock.findById(userId)).thenReturn(Optional.of(penny));
        when(userRepoMock.save(pennyWithNewFullName)).thenReturn(pennyWithNewFullName);

        boolean result = userService.changeFullName(userId, newFullName);
        assertTrue(result);
        verify(userRepoMock, times(1)).findById(userId);
        verify(userRepoMock, times(1)).save(pennyWithNewFullName);
    }

    @Test
    public void changeFullNameBad() {
        Long userId = 1L;
        User leonard = UserUtils.moderatorLeonardHofstadter(userId);

        when(userRepoMock.findById(userId)).thenReturn(Optional.of(leonard));

        boolean result = userService.changeFullName(userId, null);
        assertFalse(result);
        verify(userRepoMock, times(1)).findById(userId);
        verify(userRepoMock, never()).save(leonard);
    }

    @Test
    public void changeEmailSuccess() {
        Long userId = 1L;
        User howard = UserUtils.courierHowardWolowitz(userId);
        String newEmail = "happy-howard@bigbang.theory";
        User howardWithNewEmail = UserUtils.courierHowardWolowitz(userId);
        howardWithNewEmail.setEmail(newEmail);

        when(userRepoMock.findByEmail(newEmail)).thenReturn(null);
        when(userRepoMock.save(howardWithNewEmail)).thenReturn(howardWithNewEmail);

        String password = "password";
        EmailChangeDTO changeDTO = new EmailChangeDTO();
        changeDTO.setEmail(newEmail);
        changeDTO.setPassword(password);
        boolean result = userService.changeEmail(howard, changeDTO);
        assertTrue(result);
        verify(userRepoMock, times(1)).findByEmail(newEmail);
        verify(userRepoMock, times(1)).save(howardWithNewEmail);
    }

    @Test
    public void changeEmailAlreadyExists() {
        Long userId = 1L;
        User penny = UserUtils.clientPennyTeller(userId);
        Long anotherId = 2L;
        User leonard = UserUtils.moderatorLeonardHofstadter(anotherId);

        String newPennyEmail = leonard.getEmail();
        when(userRepoMock.findByEmail(newPennyEmail)).thenReturn(leonard);

        String password = "password";
        EmailChangeDTO changeDTO = new EmailChangeDTO();
        changeDTO.setEmail(newPennyEmail);
        changeDTO.setPassword(password);

        Exception exception = assertThrows(AlreadyExistsException.class, () -> {
           userService.changeEmail(penny, changeDTO);
        });
        String resultMessage = exception.getMessage();
        String perfectMessage = new AlreadyExistsException(newPennyEmail).getMessage();
        assertEquals(perfectMessage, resultMessage);
        verify(userRepoMock, times(1)).findByEmail(newPennyEmail);
        verify(userRepoMock, never()).save(any(User.class));
    }

    @Test
    public void changeEmailPasswordMismatch() {
        Long userId = 1L;
        User howard = UserUtils.courierHowardWolowitz(userId);
        String newEmail = "happy-howard@bigbang.theory";

        when(userRepoMock.findByEmail(newEmail)).thenReturn(null);

        String password = "wrong-password";
        EmailChangeDTO changeDTO = new EmailChangeDTO();
        changeDTO.setEmail(newEmail);
        changeDTO.setPassword(password);
        assertThrows(PasswordsMismatchException.class, ()-> {
            userService.changeEmail(howard, changeDTO);
        });
        verify(userRepoMock, times(1)).findByEmail(newEmail);
        verify(userRepoMock, never()).save(any(User.class));
    }

    @Test
    public void changePasswordSuccess() {
        Long userId = 1L;
        User rajesh = UserUtils.clientRajeshKoothrappali(userId);
        String oldPass = "password";
        String newPass = "koothrappali";
        PasswordChangeDTO passwordDTO = new PasswordChangeDTO();
        passwordDTO.setNewPassword(newPass);
        passwordDTO.setNewPasswordConfirm(newPass);
        passwordDTO.setOldPassword(oldPass);

        when(userRepoMock.save(any(User.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        boolean result = userService.changePassword(rajesh, passwordDTO);

        assertTrue(result);
        verify(userRepoMock, times(1)).save(any(User.class));
    }

    @Test
    public void changePasswordOldPassIncorrect() {
        Long userId = 1L;
        User rajesh = UserUtils.clientRajeshKoothrappali(userId);
        String oldPass = "wrongOldPass";
        String newPass = "koothrappali";
        PasswordChangeDTO passwordDTO = new PasswordChangeDTO();
        passwordDTO.setNewPassword(newPass);
        passwordDTO.setNewPasswordConfirm(newPass);
        passwordDTO.setOldPassword(oldPass);

        Exception exception = assertThrows(PasswordsMismatchException.class, () -> {
           userService.changePassword(rajesh, passwordDTO);
        });
        verify(userRepoMock, never()).save(any(User.class));
    }

    @Test
    public void changePasswordNewPassNotSame() {
        Long userId = 1L;
        User rajesh = UserUtils.clientRajeshKoothrappali(userId);
        String oldPass = "password";
        String newPass = "koothrappali";
        String newPass2 = "wrongNewPass";
        PasswordChangeDTO passwordDTO = new PasswordChangeDTO();
        passwordDTO.setNewPassword(newPass);
        passwordDTO.setNewPasswordConfirm(newPass2);
        passwordDTO.setOldPassword(oldPass);

        Exception exception = assertThrows(PasswordsMismatchException.class, () -> {
            userService.changePassword(rajesh, passwordDTO);
        });
        verify(userRepoMock, never()).save(any(User.class));
    }

    @Test
    public void getAllAdminsSuccess() {
        Long userId = 1L;
        User sheldonAdmin = UserUtils.adminSheldonCooper(userId);
        List<User> admins = new ArrayList<>();
        admins.add(sheldonAdmin);
        when(userRepoMock.findByRole(Role.ADMIN)).thenReturn(admins);

        List<UserInfoDTO> resultDTO = userService.getAllAdmins();
        List<UserInfoDTO> perfectDTO = new ArrayList<>();
        perfectDTO.add(UserUtils.createUserDTO(sheldonAdmin));
        assertEquals(perfectDTO, resultDTO);
        verify(userRepoMock, times(1)).findByRole(Role.ADMIN);
    }

    @Test
    public void getAllUsersSuccess() {
        User penny = UserUtils.clientPennyTeller(1L);
        User leonard = UserUtils.moderatorLeonardHofstadter(2L);
        List<User> users = new ArrayList<>();
        users.add(penny);
        users.add(leonard);
        Page<User> userPage = new PageImpl<>(users);
        Pageable pageable = PageRequest.of(0, 2);
        when(userRepoMock.findAll(pageable)).thenReturn(userPage);

        List<UserInfoDTO> resultDTO = userService.getAllUsers(pageable);
        List<UserInfoDTO> perfectDTO = users.stream()
                                        .map(user -> UserUtils.createUserDTO(user))
                                        .collect(Collectors.toList());
        assertEquals(perfectDTO, resultDTO);
        verify(userRepoMock, times(1)).findAll(pageable);
    }

    @Test
    public void setLastSigninSuccess() {
        User howard = UserUtils.courierHowardWolowitz(1L);
        Timestamp current = new Timestamp(System.currentTimeMillis());
        howard.setLastSigninDate(current);
        when(userRepoMock.save(any(User.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        userService.setLastSigninFromNow(howard);
        assertTrue(current.before(howard.getLastSigninDate()));
        verify(userRepoMock, times(1)).save(any());
    }

    @Test
    public void searchUsersSuccess() {
        User sheldonCooper = UserUtils.adminSheldonCooper(1L);
        List<User> users = new ArrayList<>();
        users.add(sheldonCooper);
        Page<User> userPage = new PageImpl<>(users);
        Pageable pageable = PageRequest.of(0, 2);
        String searchPhrase = "sh co";
        String resultSearchPhrase = "sh:* & co:*";

        when(userRepoMock.searchUsers(resultSearchPhrase, pageable)).thenReturn(userPage);
        List<UserInfoDTO> resultDTO = userService.searchUsers(searchPhrase, pageable);
        List<UserInfoDTO> perfectDTO = new ArrayList<>();
        perfectDTO.add(UserUtils.createUserDTO(sheldonCooper));
        assertEquals(perfectDTO, resultDTO);
        verify(userRepoMock, times(1)).searchUsers(resultSearchPhrase, pageable);

    }

    @Test
    public void lockUserSuccess() {
        User penny = UserUtils.clientPennyTeller(1L);

        when(userRepoMock.save(any(User.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        UserInfoDTO resultDTO = userService.lockUser(penny);
        assertTrue(resultDTO.getLockDate() != null);
        verify(userRepoMock, times(1)).save(any(User.class));
    }

    @Test
    public void lockUserAlsoLocked() {
        User penny = UserUtils.clientPennyTeller(1L);
        Timestamp current = new Timestamp(System.currentTimeMillis());
        penny.setLockDate(current);

        UserInfoDTO resultDTO = userService.lockUser(penny);
        assertEquals(current, resultDTO.getLockDate());
        verify(userRepoMock, never()).save(any(User.class));
    }

    @Test
    public void unlockUserSuccess() {
        User penny = UserUtils.clientPennyTeller(1L);
        Timestamp current = new Timestamp(System.currentTimeMillis());
        penny.setLockDate(current);

        UserInfoDTO resultDTO = userService.unlockUser(penny);
        assertEquals(null, resultDTO.getLockDate());
        verify(userRepoMock, times(1)).save(any(User.class));
    }

    @Test
    public void unlockUserAlsoUnlocked() {
        User penny = UserUtils.clientPennyTeller(1L);
        penny.setLockDate(null);
        UserInfoDTO resultDTO = userService.unlockUser(penny);
        assertEquals(null, resultDTO.getLockDate());
        verify(userRepoMock, never()).save(any(User.class));
    }
}
