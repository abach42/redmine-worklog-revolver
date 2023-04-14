package com.abach42.redmineworklogrevolver.Exception;

/**
 * In case an Redmine API request results empty. 
 */
public class EmptyWorklogException extends IllegalArgumentException {

	public EmptyWorklogException(String message) {
		super(message);
	}
}
