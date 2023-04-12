package com.abach42.redmineworklogrevolver.Exception;

/**
 * When configuration ends without Redmine API access key, or api call issues it
 */
public class WrongAccessKeyException extends IllegalArgumentException {

	public WrongAccessKeyException(String message) {
		super(message);
	}
}
