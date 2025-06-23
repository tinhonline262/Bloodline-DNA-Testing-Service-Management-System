package com.dna_testing_system.dev.enum;

public enum ServiceType {
    CIVIL("Civil Service"),
    ADMINISTRATIVE("Administrative Service"),
    PATERINITY_TEST("Paternity Test"),
    MATERNITY_TEST("Maternity Test"),
    GENETIC_SCREENING("Genetic Screening"),
    OTHER("Other");

    private final String description;

    ServiceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
