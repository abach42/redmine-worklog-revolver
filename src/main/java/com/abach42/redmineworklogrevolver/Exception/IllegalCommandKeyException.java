package com.abach42.redmineworklogrevolver.Exception;

/**
 * When an user hits false key. 
 */
public class IllegalCommandKeyException extends RuntimeException {

    public IllegalCommandKeyException(String message) {
        super(message);
    }
}
