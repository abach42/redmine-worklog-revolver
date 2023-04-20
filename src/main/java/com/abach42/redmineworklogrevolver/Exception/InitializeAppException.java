package com.abach42.redmineworklogrevolver.Exception;

/**
 * When configuration file misses some keys
 */
public class InitializeAppException extends ApplicationException implements Logable {

    public InitializeAppException(String message) {
        super(message);
        logServere(this);
    }
}
