package com.dna_testing_system.dev.enums;

public enum ServiceType {
    CIVIL("Dịch vụ dân sự"),
    PATERNITY_TEST("Kiểm tra quan hệ cha con");

    private final String description;

    ServiceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}