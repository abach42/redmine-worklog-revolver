package com.abach42.redmineworklogrevolver.Exception;

/*
 * In case API call fails
 */
public class ApiRequestException extends RuntimeException {
    
    public ApiRequestException(String message) {
        super(message);
    }
}
