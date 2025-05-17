package com.example.watchmaking.util.enums;

import lombok.Getter;

@Getter
public enum ServiceType {
    BATTERY_CHANGE("Battery Change"),
    BRACELET_ADJUSTMENT("Bracelet Adjustment"),
    GLASS_REPLACEMENT("Glass Replacement"),
    CLEANING("Cleaning"),
    MECHANISM_REPAIR("Mechanism Repair"),
    INSPECTION_ONLY("Inspection Only");

    private String displayName;

    ServiceType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}