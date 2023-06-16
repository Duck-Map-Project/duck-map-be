package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class NonExistentEventCategoryException extends RuntimeException {
	public NonExistentEventCategoryException() {
		super(ExceptionCodeMessage.NON_EXISTENT_EVENT_CATEGORY_EXCEPTION.message());
	}

	public NonExistentEventCategoryException(String message) {
		super(message);
	}

	public NonExistentEventCategoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public NonExistentEventCategoryException(Throwable cause) {
		super(cause);
	}

	public NonExistentEventCategoryException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
