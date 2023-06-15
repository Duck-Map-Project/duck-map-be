package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class InvalidUuidException extends RuntimeException {
	public InvalidUuidException() {
		super(ExceptionCodeMessage.INVALID_UUID_EXCEPTION.message());
	}

	public InvalidUuidException(String message) {
		super(message);
	}

	public InvalidUuidException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidUuidException(Throwable cause) {
		super(cause);
	}

	public InvalidUuidException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
