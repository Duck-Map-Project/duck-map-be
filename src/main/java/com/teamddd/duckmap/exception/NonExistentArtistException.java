package com.teamddd.duckmap.exception;

import com.teamddd.duckmap.common.ExceptionCodeMessage;

public class NonExistentArtistException extends RuntimeException {
	public NonExistentArtistException() {
		super(ExceptionCodeMessage.NON_EXISTENT_ARTIST_EXCEPTION.message());
	}

	public NonExistentArtistException(String message) {
		super(message);
	}

	public NonExistentArtistException(String message, Throwable cause) {
		super(message, cause);
	}

	public NonExistentArtistException(Throwable cause) {
		super(cause);
	}

	public NonExistentArtistException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
