package com.dna_testing_system.dev.enums;

public enum DNATestType {
    PATERNITY("Huyết thống cha - con"),
    MATERNITY("Huyết thống mẹ - con"),
    SIBLING("Huyết thống anh/chị/em"),
    IDENTITY("Xác định danh tính"),
    GENETIC("Di truyền học"),
    FORENSIC("Pháp y"),
    PRENATAL("Trước sinh"),
    Y_STR("NST Y"),
    MITOCHONDRIAL("Ty thể");

    private final String description;

    DNATestType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
