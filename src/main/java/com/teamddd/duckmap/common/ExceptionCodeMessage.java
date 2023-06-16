package com.teamddd.duckmap.common;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionCodeMessage {
	/* Auth */
	INVALID_TOKEN_EXCEPTION("A001", "유효하지 않은 토큰입니다"),
	INVALID_MEMBER_EXCEPTION("A002", "유효하지 않은 이메일 혹은 비밀번호입니다"),
	AUTHENTICATION_EXCEPTION("A003", "잘못된 인증입니다"),
	ACCESS_DENIED_EXCEPTION("A004", "권한이 없는 사용자입니다"),
	INVALID_PASSWORD_EXCEPTION("A005", "비밀번호가 맞지 않습니다"),
	INVALID_UUID_EXCEPTION("A006", "유효하지 않은 UUID 입니다"),

	/* Member */
	DUPLICATE_EMAIL_EXCEPTION("M001", "이미 사용중인 이메일입니다"),
	DUPLICATE_USERNAME_EXCEPTION("M002", "이미 사용중인 닉네임입니다"),

	/* Image File */
	NON_EXISTENT_FILE_EXCEPTION("F001", "존재하지 않는 파일입니다"),
	NOT_CONTENT_TYPE_IMAGE_EXCEPTION("F002", "이미지 파일만 저장 가능합니다"),

	/* ArtistType */
	NON_EXISTENT_ARTIST_TYPE_EXCEPTION("AT001", "잘못된 아티스트 구분 정보입니다"),

	/* Artist */
	NON_EXISTENT_ARTIST_EXCEPTION("A001", "잘못된 아티스트 정보입니다");

	private final String code;
	private final String message;

	public String code() {
		return code;
	}

	public String message() {
		return message;
	}
}
