package com.teamddd.duckmap.advice;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.teamddd.duckmap.common.ExceptionCodeMessage;
import com.teamddd.duckmap.dto.ErrorResult;
import com.teamddd.duckmap.exception.InvalidMemberException;
import com.teamddd.duckmap.exception.InvalidPasswordException;
import com.teamddd.duckmap.exception.InvalidTokenException;
import com.teamddd.duckmap.exception.InvalidUuidException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class AuthAdvice {

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler
	public ErrorResult authenticationException(AuthenticationException ex) {
		return ErrorResult.builder()
			.code(ExceptionCodeMessage.AUTHENTICATION_EXCEPTION.code())
			.message(ExceptionCodeMessage.AUTHENTICATION_EXCEPTION.message())
			.build();
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler
	public ErrorResult accessDeniedException(AccessDeniedException ex) {
		return ErrorResult.builder()
			.code(ExceptionCodeMessage.ACCESS_DENIED_EXCEPTION.code())
			.message(ExceptionCodeMessage.ACCESS_DENIED_EXCEPTION.message())
			.build();
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler
	public ErrorResult invalidTokenException(InvalidTokenException ex) {
		return ErrorResult.builder()
			.code(ExceptionCodeMessage.INVALID_TOKEN_EXCEPTION.code())
			.message(ex.getMessage())
			.build();
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler
	public ErrorResult invalidMemberException(InvalidMemberException ex) {
		return ErrorResult.builder()
			.code(ExceptionCodeMessage.INVALID_MEMBER_EXCEPTION.code())
			.message(ex.getMessage())
			.build();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler
	public ErrorResult invalidPasswordException(InvalidPasswordException ex) {
		return ErrorResult.builder()
			.code(ExceptionCodeMessage.INVALID_PASSWORD_EXCEPTION.code())
			.message(ex.getMessage())
			.build();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler
	public ErrorResult invalidUuidException(InvalidUuidException ex) {
		return ErrorResult.builder()
			.code(ExceptionCodeMessage.INVALID_UUID_EXCEPTION.code())
			.message(ex.getMessage())
			.build();
	}
}
