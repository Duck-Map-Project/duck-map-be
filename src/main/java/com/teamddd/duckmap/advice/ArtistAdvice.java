package com.teamddd.duckmap.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.teamddd.duckmap.common.ExceptionCodeMessage;
import com.teamddd.duckmap.dto.ErrorResult;
import com.teamddd.duckmap.exception.NonExistentArtistException;
import com.teamddd.duckmap.exception.NonExistentArtistTypeException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ArtistAdvice {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler
	public ErrorResult nonExistentArtistTypeException(NonExistentArtistTypeException ex) {
		return ErrorResult.builder()
			.code(ExceptionCodeMessage.NON_EXISTENT_ARTIST_TYPE_EXCEPTION.code())
			.message(ex.getMessage())
			.build();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler
	public ErrorResult nonExistentArtistException(NonExistentArtistException ex) {
		return ErrorResult.builder()
			.code(ExceptionCodeMessage.NON_EXISTENT_ARTIST_EXCEPTION.code())
			.message(ex.getMessage())
			.build();
	}
}
