package com.example.watchmaking.util.enums;

import lombok.Getter;

@Getter
public enum ServiceStatus {
    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    DONE("Done"),
    DELIVERED("Delivered"),
    CANCELED("Canceled");

    private String displayName;

    ServiceStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}