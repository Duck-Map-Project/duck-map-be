package com.teamddd.duckmap.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.teamddd.duckmap.common.ExceptionCodeMessage;
import com.teamddd.duckmap.dto.ErrorResult;
import com.teamddd.duckmap.exception.NonExistentFileException;
import com.teamddd.duckmap.exception.ServiceException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ServiceAdvice {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler
	public ErrorResult serviceException(ServiceException ex) {
		return ErrorResult.builder()
			.code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
			.message(ex.getMessage())
			.build();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler
	public ErrorResult nonExistentFileException(NonExistentFileException ex) {
		return ErrorResult.builder()
			.code(ExceptionCodeMessage.NON_EXISTENT_FILE_EXCEPTION.code())
			.message(ex.getMessage())
			.build();
	}

}
