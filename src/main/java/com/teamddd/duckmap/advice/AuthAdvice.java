package com.teamddd.duckmap.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.teamddd.duckmap.common.ExceptionCodeMessage;
import com.teamddd.duckmap.dto.ErrorResult;
import com.teamddd.duckmap.exception.InvalidTokenException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class AuthAdvice {

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler
	public ErrorResult nonExistentUserException(InvalidTokenException ex) {
		return ErrorResult.builder()
			.code(ExceptionCodeMessage.INVALID_TOKEN_EXCEPTION.code())
			.message(ex.getMessage())
			.build();
	}

}
