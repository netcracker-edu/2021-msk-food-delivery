package com.ncedu.fooddelivery.api.v1.errors.badrequest;

public class FileDeleteException extends RuntimeException {
    private static final String uuid = "9ff70de2-e7c4-460e-80d5-60461dc6c618";
    private static final String msg = "Problems with deleting file. Try again.";
    public FileDeleteException() { super(msg); }
    public String getUuid() { return uuid; }
}
