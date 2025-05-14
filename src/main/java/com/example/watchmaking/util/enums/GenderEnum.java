package com.example.watchmaking.util.enums;

public enum GenderEnum {
    MALE("Male"),
    FEMALE("Female"),
    UNISEX("Unisex");

    private String displayName;

    GenderEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
