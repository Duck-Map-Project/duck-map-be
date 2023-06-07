package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class NonExistentArtistTypeException extends RuntimeException {
	public NonExistentArtistTypeException() {
		super(ExceptionCodeMessage.NON_EXISTENT_ARTIST_TYPE_EXCEPTION.message());
	}

	public NonExistentArtistTypeException(String message) {
		super(message);
	}

	public NonExistentArtistTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NonExistentArtistTypeException(Throwable cause) {
		super(cause);
	}

	public NonExistentArtistTypeException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
