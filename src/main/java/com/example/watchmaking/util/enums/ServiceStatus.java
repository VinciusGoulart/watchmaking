package com.example.watchmaking.util.enums;

import lombok.Getter;

@Getter
public enum ServiceStatus {
    OPEN("OPEN"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE"),
    DELIVERED("DELIVERED"),
    CANCELED("CANCELED");

    private String displayName;

    ServiceStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}