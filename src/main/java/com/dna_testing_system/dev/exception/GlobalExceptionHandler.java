package com.dna_testing_system.dev.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles authentication exceptions for web requests
     * Returns the user to the login page with an error message
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleAuthenticationException(
            AuthenticationException ex, 
            Model model,
            HttpServletRequest request) {
        
        log.error("Authentication error: {} (Error Code: {})", 
                ex.getMessage(), ex.getErrorCode());
        
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", ex.getErrorCode());
        
        // Always return to login page for authentication errors
        return "login";
    }

    /**
     * Handles resource not found exceptions for web requests
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(
            ResourceNotFoundException ex,
            Model model,
            HttpServletRequest request) {
        
        log.error("Resource not found: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        
        // Return the current view or a default error page
        return getCurrentViewName(request);
    }

    
    /**
     * Handles all other exceptions for web requests
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(
            Exception ex,
            Model model) {
        
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
        
        return "error";
    }
    
    /**
     * Extracts the current view name from the request
     */
    private String getCurrentViewName(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        
        if (!contextPath.isEmpty() && uri.startsWith(contextPath)) {
            uri = uri.substring(contextPath.length());
        }
        
        if (uri.startsWith("/")) {
            uri = uri.substring(1);
        }
        
        // Default to home page if URI is empty
        if (uri.isEmpty()) {
            return "home";
        }
        
        // Handle common patterns
        if (uri.startsWith("users/")) {
            return "user-details";
        }
        
        if (uri.startsWith("tests/")) {
            return "test-details";
        }
        
        // Remove extensions if present
        if (uri.contains(".")) {
            uri = uri.substring(0, uri.lastIndexOf("."));
        }
        
        return uri;
    }
}
