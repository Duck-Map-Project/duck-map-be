package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class InvalidMemberException extends RuntimeException {
	public InvalidMemberException() {
		super(ExceptionCodeMessage.INVALID_MEMBER_EXCEPTION.message());
	}

	public InvalidMemberException(String message) {
		super(message);
	}

	public InvalidMemberException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidMemberException(Throwable cause) {
		super(cause);
	}

	public InvalidMemberException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
