package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class NonExistentEventException extends RuntimeException {
	public NonExistentEventException() {
		super(ExceptionCodeMessage.NON_EXISTENT_EVENT_EXCEPTION.message());
	}

	public NonExistentEventException(String message) {
		super(message);
	}

	public NonExistentEventException(String message, Throwable cause) {
		super(message, cause);
	}

	public NonExistentEventException(Throwable cause) {
		super(cause);
	}

	public NonExistentEventException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
