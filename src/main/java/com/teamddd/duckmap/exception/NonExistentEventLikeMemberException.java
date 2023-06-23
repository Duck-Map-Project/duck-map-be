package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class NonExistentEventLikeMemberException extends RuntimeException {
	public NonExistentEventLikeMemberException() {
		super(ExceptionCodeMessage.NON_EXISTENT_EVENT_LIKE_MEMBER_EXCEPTION.message());
	}

	public NonExistentEventLikeMemberException(String message) {
		super(message);
	}

	public NonExistentEventLikeMemberException(String message, Throwable cause) {
		super(message, cause);
	}

	public NonExistentEventLikeMemberException(Throwable cause) {
		super(cause);
	}

	public NonExistentEventLikeMemberException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
