package com.teamddd.duckmap.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.teamddd.duckmap.common.ExceptionCodeMessage;
import com.teamddd.duckmap.dto.ErrorResult;
import com.teamddd.duckmap.exception.DuplicateEmailException;
import com.teamddd.duckmap.exception.DuplicateUsernameException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class MemberAdvice {
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler
	public ErrorResult duplicateEmailException(DuplicateEmailException ex) {
		return ErrorResult.builder()
			.code(ExceptionCodeMessage.DUPLICATE_EMAIL_EXCEPTION.code())
			.message(ex.getMessage())
			.build();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler
	public ErrorResult duplicateUsernameException(DuplicateUsernameException ex) {
		return ErrorResult.builder()
			.code(ExceptionCodeMessage.DUPLICATE_USERNAME_EXCEPTION.code())
			.message(ex.getMessage())
			.build();
	}
}
