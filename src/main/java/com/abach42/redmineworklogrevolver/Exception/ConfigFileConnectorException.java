package com.abach42.redmineworklogrevolver.Exception;

/*
 * Something goes wrong with config filek handling
 */
public class ConfigFileConnectorException extends RuntimeException {
    public ConfigFileConnectorException(String message) {
        super(message);
    }
}