package com.dna_testing_system.dev.enums;

public enum CollectionMethod {
    HOME_VISIT("Thu mẫu tại nhà"),
    FACILITY("Thu mẫu tại cơ sở"),
    SELF_COLLECT("Tự thu mẫu");

    private final String displayName;

    CollectionMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}