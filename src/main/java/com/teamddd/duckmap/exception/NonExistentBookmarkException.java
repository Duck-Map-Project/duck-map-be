package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class NonExistentBookmarkException extends RuntimeException {
	public NonExistentBookmarkException() {
		super(ExceptionCodeMessage.NON_EXISTENT_BOOKMARK_FOLDER_EXCEPTION.message());
	}

	public NonExistentBookmarkException(String message) {
		super(message);
	}

	public NonExistentBookmarkException(String message, Throwable cause) {
		super(message, cause);
	}

	public NonExistentBookmarkException(Throwable cause) {
		super(cause);
	}

	public NonExistentBookmarkException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
