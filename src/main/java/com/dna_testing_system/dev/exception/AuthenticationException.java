package com.dna_testing_system.dev.exception;

import lombok.Getter;

/**
 * Exception thrown when authentication fails.
 * This can be due to invalid credentials, expired tokens, or account lockouts.
 */
@Getter
public class AuthenticationException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    /**
     * Creates a new authentication exception with the specified error code
     * and uses the default message for that error code.
     * 
     * @param errorCode The specific error code
     */
    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    
    /**
     * Creates a new authentication exception with the specified error code
     * and custom message.
     * 
     * @param errorCode The specific error code
     * @param message Custom error message
     */
    public AuthenticationException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * Creates a new authentication exception with the specified error code,
     * custom message, and cause.
     * 
     * @param errorCode The specific error code
     * @param message Custom error message
     * @param cause The cause of the exception
     */
    public AuthenticationException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    /**
     * Creates a new authentication exception with UNKNOWN_ERROR error code
     * and the specified message.
     * 
     * @param message The error message
     */
    public AuthenticationException(String message) {
        super(message);
        this.errorCode = ErrorCode.UNKNOWN_ERROR;
    }
    
    /**
     * Creates a new authentication exception with UNKNOWN_ERROR error code,
     * the specified message, and cause.
     * 
     * @param message The error message
     * @param cause The cause of the exception
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = ErrorCode.UNKNOWN_ERROR;
    }
}
