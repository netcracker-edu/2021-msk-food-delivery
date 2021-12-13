package com.ncedu.fooddelivery.api.v1.errors.badrequest;

public class FileStorageException extends RuntimeException {
    private static final String uuid = "a548d690-a5a6-4e4b-aca3-788ee806e99b";
    private static final String msg = "Problems with storing current file. Try later.";
    public FileStorageException() {super(msg);}
    public String getUuid() {return this.uuid;}
}
