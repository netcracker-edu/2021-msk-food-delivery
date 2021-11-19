package com.ncedu.fooddelivery.api.v1.dto;

import java.sql.Timestamp;
import java.util.UUID;

public class UserCommonInfoDTO {
    private String role;
    private String fullName;
    private String email;
    private Timestamp lastSigninDate;
    private UUID avatarId;


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

    public UserCommonInfoDTO(String role, String fullName, String email, Timestamp lastSigninDate, UUID avatarId) {
        this.role = role;
        this.fullName = fullName;
        this.email = email;
        this.lastSigninDate = lastSigninDate;
        this.avatarId = avatarId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
}
