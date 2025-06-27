package com.dna_testing_system.dev.enums;

public enum Gender {
    MALE("MALE"),
    FEMALE("FEMALE"),
    OTHER("OTHER");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isValid(String gender) {
        if (gender == null) return false;
        for (Gender g : Gender.values()) {
            if (g.getValue().equalsIgnoreCase(gender)) {
                return true;
            }
        }
        return false;
    }
}