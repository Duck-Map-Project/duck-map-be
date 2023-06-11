package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class NotContentTypeImageException extends RuntimeException {
	public NotContentTypeImageException() {
		super(ExceptionCodeMessage.NOT_CONTENT_TYPE_IMAGE_EXCEPTION.message());
	}

	public NotContentTypeImageException(String message) {
		super(message);
	}

	public NotContentTypeImageException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotContentTypeImageException(Throwable cause) {
		super(cause);
	}

	public NotContentTypeImageException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
