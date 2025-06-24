package com.dna_testing_system.dev.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SampleType {
    BLOOD("BLD", "Mẫu Máu"),
    SALIVA("SAL", "Mẫu Nước Bọt"),
    HAIR("HAIR", "Mẫu Tóc"),
    TISSUE("TIS", "Mẫu Mô"),
    SWAB("SWB", "Mẫu Gạc"),
    OTHER("OTH", "Mẫu Khác");

    private final String code;
    private final String description;
}