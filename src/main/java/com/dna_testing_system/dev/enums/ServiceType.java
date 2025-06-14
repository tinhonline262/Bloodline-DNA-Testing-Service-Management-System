package com.dna_testing_system.dev.enums;

public enum ServiceType {
    CIVIL("Civil Service"),
    ADMINISTRATIVE("Administrative Service");

    private final String description;

    ServiceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
