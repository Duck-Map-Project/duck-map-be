package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class NonExistentBookmarkFolderException extends RuntimeException {
	public NonExistentBookmarkFolderException() {
		super(ExceptionCodeMessage.NON_EXISTENT_BOOKMARK_FOLDER_EXCEPTION.message());
	}

	public NonExistentBookmarkFolderException(String message) {
		super(message);
	}

	public NonExistentBookmarkFolderException(String message, Throwable cause) {
		super(message, cause);
	}

	public NonExistentBookmarkFolderException(Throwable cause) {
		super(cause);
	}

	public NonExistentBookmarkFolderException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
