package com.example.watchmaking.util.enums;

import lombok.Getter;

@Getter
public enum ServiceType {
    BATTERY_CHANGE("BATTERY_CHANGE"),
    BRACELET_ADJUSTMENT("BRACELET_ADJUSTMENT"),
    GLASS_REPLACEMENT("GLASS_REPLACEMENT"),
    CLEANING("CLEANING"),
    MECHANISM_REPAIR("MECHANISM_REPAIR"),
    INSPECTION_ONLY("INSPECTION_ONLY");

    private String displayName;

    ServiceType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}