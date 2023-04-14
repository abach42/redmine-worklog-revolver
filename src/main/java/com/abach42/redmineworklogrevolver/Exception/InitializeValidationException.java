package com.abach42.redmineworklogrevolver.Exception;

/*
 * Special case for validation after init
 */
public class InitializeValidationException extends InitializeAppException {
    
    public InitializeValidationException(String message) {
        super(message);
    }
}
