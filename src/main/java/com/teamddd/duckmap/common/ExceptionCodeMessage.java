package com.teamddd.duckmap.common;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionCodeMessage {
	INVALID_TOKEN_EXCEPTION("A001", "유효하지 않은 토큰입니다"),
	INVALID_MEMBER_EXCEPTION("A002", "유효하지 않은 이메일 혹은 비밀번호입니다");

	private final String code;
	private final String message;

	public String code() {
		return code;
	}

	public String message() {
		return message;
	}
}
