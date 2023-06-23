package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class NonExistentMemberException extends RuntimeException {
	public NonExistentMemberException() {
		super(ExceptionCodeMessage.NON_EXISTENT_MEMBER_EXCEPTION.message());
	}

	public NonExistentMemberException(String message) {
		super(message);
	}

	public NonExistentMemberException(String message, Throwable cause) {
		super(message, cause);
	}

	public NonExistentMemberException(Throwable cause) {
		super(cause);
	}

	public NonExistentMemberException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
