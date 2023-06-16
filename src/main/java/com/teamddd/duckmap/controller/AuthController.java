package com.teamddd.duckmap.controller;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamddd.duckmap.config.security.TokenDto;
import com.teamddd.duckmap.dto.user.auth.LoginReq;
import com.teamddd.duckmap.dto.user.auth.SendResetPasswordEmailReq;
import com.teamddd.duckmap.dto.user.auth.SendResetPasswordEmailRes;
import com.teamddd.duckmap.service.AuthService;
import com.teamddd.duckmap.service.MemberService;
import com.teamddd.duckmap.service.SendMailService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;
	private final MemberService memberService;
	private final SendMailService mailService;
	private static final long COOKIE_EXPIRATION = 604800;

	@Operation(summary = "로그인")
	@PostMapping("/login")
	public ResponseEntity<?> login(@Validated @RequestBody LoginReq loginUserRQ) {

		// User 등록 및 Refresh Token 저장
		TokenDto tokenDto = authService.login(loginUserRQ);

		// RT 저장
		HttpCookie httpCookie = ResponseCookie.from("refresh-token", tokenDto.getRefreshToken())
			.maxAge(COOKIE_EXPIRATION)
			.httpOnly(true)
			.secure(true)
			.build();
		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, httpCookie.toString())
			// AT 저장
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
			.build();
	}

	//토큰 유효성 확인
	@Operation(summary = "access 토큰 유효성 확인")
	@PostMapping("/validate")
	public void validate(@RequestHeader("Authorization") String requestAccessToken) {
		authService.validate(requestAccessToken);
	}

	// 토큰 재발급
	@Operation(summary = "access&refresh 토큰 재발급")
	@PostMapping("/reissue")
	public ResponseEntity<?> reissue(@CookieValue(name = "refresh-token") String requestRefreshToken) {
		log.info(requestRefreshToken);
		TokenDto reissuedTokenDto = authService.reissue(requestRefreshToken);

		if (reissuedTokenDto != null) { // 토큰 재발급 성공
			// RT 저장
			ResponseCookie responseCookie = ResponseCookie.from("refresh-token", reissuedTokenDto.getRefreshToken())
				.maxAge(COOKIE_EXPIRATION)
				.httpOnly(true)
				.secure(true)
				.build();
			return ResponseEntity
				.status(HttpStatus.OK)
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				// AT 저장
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + reissuedTokenDto.getAccessToken())
				.build();

		} else { // Refresh Token 탈취 가능성
			// Cookie 삭제 후 재로그인 유도
			ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
				.maxAge(0)
				.path("/")
				.build();
			return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.build();
		}
	}

	// 로그아웃
	@Operation(summary = "로그아웃")
	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader("Authorization") String requestAccessToken) {
		authService.logout(requestAccessToken);
		ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
			.maxAge(0)
			.path("/")
			.build();

		return ResponseEntity
			.status(HttpStatus.OK)
			.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
			.build();
	}

	//UUID 생성 및 이메일 전송
	@Operation(summary = "UUID 생성 및 이메일 전송")
	@PostMapping("/send-reset-password")
	public SendResetPasswordEmailRes sendResetPassword(
		@Validated @RequestBody SendResetPasswordEmailReq resetPasswordEmailReq) {
		memberService.checkMemberByEmail(resetPasswordEmailReq.getEmail());
		String uuid = mailService.sendResetPasswordEmail(resetPasswordEmailReq.getEmail());
		return SendResetPasswordEmailRes.builder()
			.UUID(uuid)
			.build();
	}

}

