package com.ncedu.fooddelivery.api.v1.controllers;

import com.ncedu.fooddelivery.api.v1.dto.user.*;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    //TODO: finish PATCH role endpoint
    //TODO: add changing warehouse for moderator and courier
    //TODO: delete photo from user (only Admin)
    //TODO: blocking user (only Admin)
    //TODO: search for any field (incomplete text search)
    //TODO: trigger on deleting non active users
    //TODO: add unit tests

    @Autowired UserService userService;
    @Autowired ClientService clientService;
    @Autowired ModeratorService moderatorService;

    @GetMapping("/api/v1/user/{id}")
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
    public UserInfoDTO getUserById(
            @PathVariable Long id,
            @AuthenticationPrincipal User authedUser) {
        UserInfoDTO userInfo = userService.getUserDTOById(id);
        if (userInfo == null) {
            throw new NotFoundEx(id.toString());
        }

        //get extended info depending on the role of the requested user
        String userRole = userInfo.getRole();
        Long userId = userInfo.getId();
        if (Role.isCLIENT(userRole)) {
            ClientInfoDTO clientInfo = clientService.getClientDTOById(userId);
            return clientInfo;
        }
        if (Role.isMODERATOR(userRole)) {
            ModeratorInfoDTO moderatorInfo = moderatorService.getModeratorDTOById(userId);
            return moderatorInfo;
        }
        log.debug("Sending user with id: " + id);
        //if user role is ADMIN
        return userInfo;
    }

    @PutMapping("/api/v1/user/{id}")
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
    public ResponseEntity<?> changeUserInfo(
            @PathVariable Long id,
            @Valid @RequestBody UserChangeInfoDTO newUserInfo) {
        User user = userService.getUserById(id);
        String userRole = user.getRole().name();
        boolean isModified = false;
        if (Role.isCLIENT(userRole)) {
            isModified = clientService.changeClientInfo(id, newUserInfo);
        }
        //for admin and moderator we can change only full name
        String newFullName = newUserInfo.getFullName();
        isModified = userService.changeFullName(id, newFullName);
        log.debug("User with id " + id +" modified " +isModified);
        return createModifyResponse(isModified);
    }

    @DeleteMapping("/api/v1/user/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id) {
        boolean isDeleted = userService.deleteUserById(id);
        if (isDeleted) {
            log.debug("Deleted user " + id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        log.warn("Problems with deleting user: " + id);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @PatchMapping("/api/v1/user/{id}/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> changeUserRole(
            @PathVariable Long id,
            @RequestBody Role role) {
        System.out.println("Requested id: " + id);
        System.out.println("Requested role: " + role);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity<?> createModifyResponse(boolean isModified) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("isModified", isModified);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/api/v1/user/{user}/lock")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public UserInfoDTO lockUser(@PathVariable User user) {
        return userService.lockUser(user);
    }

    @PatchMapping("/api/v1/user/{user}/unlock")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public UserInfoDTO unlockUser(@PathVariable User user) {
        return userService.unlockUser(user);
    }

    @DeleteMapping("/api/v1/user/{user}/avatar")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> deleteAvatar(
            @PathVariable User targetUser,
            @AuthenticationPrincipal User authedUser) {
        userService.deleteAvatar(targetUser, authedUser);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/api/v1/admins")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserInfoDTO> getAdminList() {
        List<UserInfoDTO> userList = userService.getAllAdmins();
        return userList;
    }

    @GetMapping("/api/v1/users")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public List<UserInfoDTO> getUserList(
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable
    ) {
        List<UserInfoDTO> userList = userService.getAllUsers(pageable);
        return userList;
    }

    @GetMapping("/api/v1/users/search")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public List<UserInfoDTO> searchUsers(
        @Valid @RequestBody UserSearchDTO searchDTO,
        @PageableDefault(sort = { "rank" }, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        String phrase = searchDTO.getPhrase();
        return userService.searchUsers(phrase, pageable);
    }
}
