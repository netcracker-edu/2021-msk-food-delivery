package com.ncedu.fooddelivery.api.v1.dto;

import java.sql.Timestamp;
import java.util.UUID;

public class UserDTO {
    private Long id;
    private String role;
    private String password;
    private String fullName;
    private String email;
    private Timestamp regDate;
    private Timestamp lastSigninDate;
    private UUID avatarId;
    private Timestamp lockDate;


    public static String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(byteToHex(byteArray[i]));
        }
        return hexStringBuffer.toString();
    }
    private static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    public UserDTO(Long id, String role, String password, String fullName, String email, Timestamp regDate, Timestamp lastSigninDate, UUID avatarId, Timestamp lockDate) {
        this.id = id;
        this.role = role;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.regDate = regDate;
        this.lastSigninDate = lastSigninDate;
        this.avatarId = avatarId;
        this.lockDate = lockDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getRegDate() {
        return regDate;
    }

    public void setRegDate(Timestamp regDate) {
        this.regDate = regDate;
    }

    public Timestamp getLastSigninDate() {
        return lastSigninDate;
    }

    public void setLastSigninDate(Timestamp lastSigninDate) {
        this.lastSigninDate = lastSigninDate;
    }

    public UUID getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(UUID avatarId) {
        this.avatarId = avatarId;
    }

    public Timestamp getLockDate() {
        return lockDate;
    }

    public void setLockDate(Timestamp lockDate) {
        this.lockDate = lockDate;
    }
}
