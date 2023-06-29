package com.abach42.redmineworklogrevolver.Exception;

/*
 * In case API call fails
 */
public class ApiRequestException extends RuntimeException 
    implements Logable {
    
    public ApiRequestException(String message) {
        super(message);
        //logServere(this);
    }
}
