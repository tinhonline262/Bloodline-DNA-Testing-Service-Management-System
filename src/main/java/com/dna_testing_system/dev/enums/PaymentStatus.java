package com.dna_testing_system.dev.enums;

public enum PaymentStatus {
    PENDING("Pending"),
    PAID("Paid");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
