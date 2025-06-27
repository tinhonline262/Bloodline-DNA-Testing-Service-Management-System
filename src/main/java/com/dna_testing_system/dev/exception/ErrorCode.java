package com.dna_testing_system.dev.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_CREDENTIALS("Invalid username or password"),
    USERNAME_INVALID("Username must be at least 3 characters"),
    NOT_FOUND("Not Found"),
    USER_EXISTS("User already exists"),
    EMAIL_EXISTS("Email already exists, please use another one"),
    USER_NOT_EXISTS("User does not exist"),
    PASSWORD_INVALID("Password must be at least 8 characters"),

    SERVICE_TYPE_EXISTS("Service type already exists"),
    SERVICE_TYPE_NOT_EXISTS("Service type does not exist"),
    SERVICE_FEATURE_NOT_EXISTS("Service feature does not exist"),
    MEDICAL_SERVICE_NOT_EXISTS("Medical service does not exist"),

    // ✅ ADD THESE NEW ONES BASED ON YOUR SERVICE LAYER
    INVALID_CUSTOMER_ID("Invalid customer ID"),
    INVALID_COLLECTION_TYPE("Invalid collection type"),
    INVALID_APPOINTMENT_DATE("Invalid appointment date"),
    ORDER_NOT_EXISTS("Order does not exist"),
    ORDER_ALREADY_CONFIRMED("Order already confirmed"),
    INSUFFICIENT_PARTICIPANTS("Insufficient number of participants"),
    NO_PARTICIPANTS("No participants provided"),
    TOO_MANY_PARTICIPANTS("Too many participants"),
    INVALID_PARTICIPANT_NAME("Participant name is invalid"),
    INVALID_PARTICIPANT_DOB("Participant date of birth is invalid"),

    // ✅ Thêm các hằng số bị thiếu
    MEDICAL_SERVICE_NOT_AVAILABLE("Medical service is not available"),
    INVALID_PARTICIPANT_GENDER("Participant gender is invalid"),

    UNKNOWN_ERROR("Unknown error!");

    private String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}