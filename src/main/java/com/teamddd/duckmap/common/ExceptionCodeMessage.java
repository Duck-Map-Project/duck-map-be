package com.teamddd.duckmap.common;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionCodeMessage {
	INVALID_TOKEN_EXCEPTION("A001", "유효하지 않은 토큰입니다"),
	INVALID_MEMBER_EXCEPTION("A002", "유효하지 않은 이메일 혹은 비밀번호입니다"),
	AUTHENTICATION_EXCEPTION("A003", "인증이 필요한 요청입니다"),
	ACCESS_DENIED_EXCEPTION("A004", "권한이 없는 사용자입니다"),

	DUPLICATE_EMAIL_EXCEPTION("M001", "이미 사용중인 이메일입니다"),
	DUPLICATE_USERNAME_EXCEPTION("M002", "이미 사용중인 닉네임입니다");

	private final String code;
	private final String message;

	public String code() {
		return code;
	}

	public String message() {
		return message;
	}
}