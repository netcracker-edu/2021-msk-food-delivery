package com.ncedu.fooddelivery.api.v1.entities;

public enum FileType {
    JPEG("image/jpeg"), PNG("image/png");

    private final String mediaType;

    FileType(String mediaType) {
        this.mediaType = mediaType;
    }

    public static boolean isCorrectExt(String ext) {
        if (ext == null) {
            return false;
        }
        for (FileType f : FileType.values()) {
            if (ext.equals(f.name())) {
                return true;
            }
        }
        return false;
    }

    public static String getMediaTypeByExt(String ext) {
        String mediaType = null;
        if (!isCorrectExt(ext)) {
            return mediaType;
        }
        for (FileType f : FileType.values()) {
            if (ext.equals(f.name())) {
                mediaType = f.getMediaType();
            }
        }
        return mediaType;
    }

    public String getMediaType() {
        return this.mediaType;
    }

}
