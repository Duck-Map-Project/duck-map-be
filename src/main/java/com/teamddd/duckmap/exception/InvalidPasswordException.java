package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class InvalidPasswordException extends RuntimeException {
	public InvalidPasswordException() {
		super(ExceptionCodeMessage.INVALID_PASSWORD_EXCEPTION.message());
	}

	public InvalidPasswordException(String message) {
		super(message);
	}

	public InvalidPasswordException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidPasswordException(Throwable cause) {
		super(cause);
	}

	public InvalidPasswordException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
