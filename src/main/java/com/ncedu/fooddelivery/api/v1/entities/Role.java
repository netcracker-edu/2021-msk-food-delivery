package com.ncedu.fooddelivery.api.v1.entities;

public enum Role {
    ADMIN, CLIENT, COURIER, MODERATOR;


    public static boolean isADMIN(String role) {
        final String ADMIN_ROLE = Role.ADMIN.name();
        return ADMIN_ROLE.equals(role);
    }
    public static boolean isCLIENT(String role) {
        final String CLIENT_ROLE = Role.CLIENT.name();
        return CLIENT_ROLE.equals(role);
    }
    public static boolean isMODERATOR(String role) {
        final String MODERATOR_ROLE = Role.MODERATOR.name();
        return MODERATOR_ROLE.equals(role);
    }
    public static boolean isCOURIER(String role) {
        final String COURIER_ROLE = Role.COURIER.name();
        return COURIER_ROLE.equals(role);
    }
}
