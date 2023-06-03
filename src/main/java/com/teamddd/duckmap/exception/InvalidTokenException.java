package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class InvalidTokenException extends RuntimeException {
	public InvalidTokenException() {
		super(ExceptionCodeMessage.INVALID_TOKEN_EXCEPTION.message());
	}

	public InvalidTokenException(String message) {
		super(message);
	}

	public InvalidTokenException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidTokenException(Throwable cause) {
		super(cause);
	}

	public InvalidTokenException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
