package com.abach42.redmineworklogrevolver.Exception;

/*
 * When time range factory fails
 */
public class TimeRangeFactoryException extends IllegalCommandKeyException {

    public TimeRangeFactoryException(String message) {
        super(message);
    }
}