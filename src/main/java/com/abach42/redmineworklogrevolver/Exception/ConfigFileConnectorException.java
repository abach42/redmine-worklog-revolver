package com.abach42.redmineworklogrevolver.Exception;

/*
 * Something goes wrong with config filek handling
 */
public class ConfigFileConnectorException extends RuntimeException 
    implements Logable {
    
    public ConfigFileConnectorException(String message) {
        super(message);
        logServere(this);
    }
}