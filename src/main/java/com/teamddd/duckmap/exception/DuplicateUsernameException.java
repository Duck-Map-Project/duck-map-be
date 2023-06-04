package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class DuplicateUsernameException extends RuntimeException {
	public DuplicateUsernameException() {
		super(ExceptionCodeMessage.DUPLICATE_USERNAME_EXCEPTION.message());
	}

	public DuplicateUsernameException(String message) {
		super(message);
	}

	public DuplicateUsernameException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateUsernameException(Throwable cause) {
		super(cause);
	}

	public DuplicateUsernameException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
