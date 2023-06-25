package com.teamddd.duckmap.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.teamddd.duckmap.common.ExceptionCodeMessage;
import com.teamddd.duckmap.dto.ErrorResult;
import com.teamddd.duckmap.exception.NonExistentBookmarkException;
import com.teamddd.duckmap.exception.NonExistentBookmarkFolderException;
import com.teamddd.duckmap.exception.NonExistentEventCategoryException;
import com.teamddd.duckmap.exception.NonExistentEventException;
import com.teamddd.duckmap.exception.NonExistentEventLikeException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class EventAdvice {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler
	public ErrorResult nonExistentEventCategoryException(NonExistentEventCategoryException ex) {
		return ErrorResult.builder()
			.code(ExceptionCodeMessage.NON_EXISTENT_EVENT_CATEGORY_EXCEPTION.code())
			.message(ex.getMessage())
			.build();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler
	public ErrorResult nonExistentEventException(NonExistentEventException ex) {
		return ErrorResult.builder()
			.code(ExceptionCodeMessage.NON_EXISTENT_EVENT_EXCEPTION.code())
			.message(ex.getMessage())
			.build();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler
	public ErrorResult nonExistentEventLikeException(NonExistentEventLikeException ex) {
		return ErrorResult.builder()
			.code(ExceptionCodeMessage.NON_EXISTENT_EVENT_LIKE_EXCEPTION.code())
			.message(ex.getMessage())
			.build();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler
	public ErrorResult nonExistentBookmarkFolderException(NonExistentBookmarkFolderException ex) {
		return ErrorResult.builder()
			.code(ExceptionCodeMessage.NON_EXISTENT_BOOKMARK_FOLDER_EXCEPTION.code())
			.message(ex.getMessage())
			.build();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler
	public ErrorResult nonExistentBookmarkException(NonExistentBookmarkException ex) {
		return ErrorResult.builder()
			.code(ExceptionCodeMessage.NON_EXISTENT_BOOKMARK_EXCEPTION.code())
			.message(ex.getMessage())
			.build();
	}
}
