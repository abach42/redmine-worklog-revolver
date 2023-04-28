package com.abach42.redmineworklogrevolver.Exception;

/**
 * In case an Redmine API request results empty. 
 */
public class EmptyResultException extends IllegalArgumentException 
    implements Logable {

    public EmptyResultException(String message) {
        super(message);
        logServere(this);
    }

}
