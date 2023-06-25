package com.teamddd.duckmap.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.teamddd.duckmap.common.ExceptionCodeMessage;
import com.teamddd.duckmap.dto.ErrorResult;
import com.teamddd.duckmap.exception.NonExistentReviewException;

public class ReviewAdvice {
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler
	public ErrorResult nonExistentReviewException(NonExistentReviewException ex) {
		return ErrorResult.builder()
			.code(ExceptionCodeMessage.NON_EXISTENT_REVIEW_EXCEPTION.code())
			.message(ex.getMessage())
			.build();
	}
}
