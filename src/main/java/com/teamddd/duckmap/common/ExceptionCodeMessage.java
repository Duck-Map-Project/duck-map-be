package com.teamddd.duckmap.common;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionCodeMessage {
	INVALID_TOKEN_EXCEPTION("A001", "유요하지 않은 토큰입니다");

	private final String code;
	private final String message;

	public String code() {
		return code;
	}

	public String message() {
		return message;
	}
}
