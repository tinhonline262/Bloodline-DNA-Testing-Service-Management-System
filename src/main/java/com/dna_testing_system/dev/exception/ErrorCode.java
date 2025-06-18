package com.dna_testing_system.dev.exception;


import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_CREDENTIALS("Invalid username or password"),
    USERNAME_INVALID( "Username must be least 3 characters"),
    NOT_FOUND( "Not Found"),
    USER_EXISTS("User is exists"),
    EMAIL_EXISTS("Email is exists, please use other email address"),
    USER_NOT_EXISTS("User is not exists"),
    PASSWORD_INVALID("Password must be least 8 characters"),
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
