package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class NonExistentReviewException extends RuntimeException {
	public NonExistentReviewException() {
		super(ExceptionCodeMessage.NON_EXISTENT_REVIEW_EXCEPTION.message());
	}

	public NonExistentReviewException(String message) {
		super(message);
	}

	public NonExistentReviewException(String message, Throwable cause) {
		super(message, cause);
	}

	public NonExistentReviewException(Throwable cause) {
		super(cause);
	}

	public NonExistentReviewException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
