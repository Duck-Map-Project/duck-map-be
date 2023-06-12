package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class NonExistentFileException extends RuntimeException {
	public NonExistentFileException() {
		super(ExceptionCodeMessage.NON_EXISTENT_FILE_EXCEPTION.message());
	}

	public NonExistentFileException(String path) {
		super(ExceptionCodeMessage.NON_EXISTENT_FILE_EXCEPTION.message() + ": " + path);
	}

	public NonExistentFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public NonExistentFileException(Throwable cause) {
		super(cause);
	}

	public NonExistentFileException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
