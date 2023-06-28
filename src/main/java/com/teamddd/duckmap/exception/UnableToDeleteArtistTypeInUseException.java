package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class UnableToDeleteArtistTypeInUseException extends RuntimeException {
	public UnableToDeleteArtistTypeInUseException() {
		super(ExceptionCodeMessage.UNABLE_TO_DELETE_ARTIST_TYPE_IN_USE_EXCEPTION.message());
	}

	public UnableToDeleteArtistTypeInUseException(String message) {
		super(message);
	}

	public UnableToDeleteArtistTypeInUseException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnableToDeleteArtistTypeInUseException(Throwable cause) {
		super(cause);
	}

	public UnableToDeleteArtistTypeInUseException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
