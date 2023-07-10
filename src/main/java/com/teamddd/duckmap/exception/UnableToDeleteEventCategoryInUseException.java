package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class UnableToDeleteEventCategoryInUseException extends RuntimeException {
	public UnableToDeleteEventCategoryInUseException() {
		super(ExceptionCodeMessage.UNABLE_TO_DELETE_EVENT_CATEGORY_IN_USE_EXCEPTION.message());
	}

	public UnableToDeleteEventCategoryInUseException(String message) {
		super(message);
	}

	public UnableToDeleteEventCategoryInUseException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnableToDeleteEventCategoryInUseException(Throwable cause) {
		super(cause);
	}

	public UnableToDeleteEventCategoryInUseException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
