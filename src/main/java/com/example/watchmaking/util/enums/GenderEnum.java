package com.example.watchmaking.util.enums;

import lombok.Getter;

@Getter
public enum GenderEnum {
    MALE("MALE"),
    FEMALE("FEMALE"),
    UNISEX("UNISEX");

    private String displayName;

    GenderEnum(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
