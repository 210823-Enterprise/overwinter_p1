package com.overwinter.exceptions;

public class NoPrimaryKeyException extends RuntimeException{
	private static final long serialVersionUID = -2458493368683232707L;

	public NoPrimaryKeyException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public NoPrimaryKeyException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NoPrimaryKeyException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NoPrimaryKeyException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
