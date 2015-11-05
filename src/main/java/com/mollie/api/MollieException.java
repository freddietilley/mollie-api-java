package com.mollie.api;

@SuppressWarnings("serial")
public class MollieException extends Exception {
	public final String type;
	public final String message;

	public MollieException(String message) {
		super(message);
		this.type = null;
		this.message = message;
	}

	public MollieException(String type, String message) {
		super("API Error ("+type+"): "+message);
		this.type = type;
		this.message = message;
	}
}
