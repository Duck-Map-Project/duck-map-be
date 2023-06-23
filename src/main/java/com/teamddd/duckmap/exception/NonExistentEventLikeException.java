package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class NonExistentEventLikeException extends RuntimeException {
	public NonExistentEventLikeException() {
		super(ExceptionCodeMessage.NON_EXISTENT_EVENT_LIKE_EXCEPTION.message());
	}

	public NonExistentEventLikeException(String message) {
		super(message);
	}

	public NonExistentEventLikeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NonExistentEventLikeException(Throwable cause) {
		super(cause);
	}

	public NonExistentEventLikeException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
