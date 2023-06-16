package com.teamddd.duckmap.service;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.config.security.JwtProvider;
import com.teamddd.duckmap.config.security.TokenDto;
import com.teamddd.duckmap.dto.user.auth.LoginReq;
import com.teamddd.duckmap.exception.InvalidTokenException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtProvider jwtProvider;
	private final RedisService redisService;
	private final String SERVER = "Server";

	// 로그인: 인증 정보 저장 및 비어 토큰 발급
	@Transactional
	public TokenDto login(LoginReq loginUserRQ) {
		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(loginUserRQ.getEmail(), loginUserRQ.getPassword());

		Authentication authentication = authenticationManagerBuilder.getObject()
			.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return generateToken(SERVER, authentication.getName(), getAuthorities(authentication));
	}

	// 토큰 발급
	@Transactional
	public TokenDto generateToken(String provider, String email, String authorities) {
		// RT가 이미 있을 경우
		if (redisService.getValues("RT(" + provider + "):" + email) != null) {
			redisService.deleteValues("RT(" + provider + "):" + email); // 삭제
		}

		// AT, RT 생성 및 Redis에 RT 저장
		TokenDto tokenDto = jwtProvider.createToken(email, authorities);
		saveRefreshToken(provider, email, tokenDto.getRefreshToken());
		return tokenDto;
	}

	// AT가 만료일자만 초과한 유효한 토큰인지 검사
	public void validate(String requestAccessTokenInHeader) {
		String requestAccessToken = resolveToken(requestAccessTokenInHeader);
		if (!jwtProvider.validateAccessTokenOnlyExpired(requestAccessToken)) {
			throw new InvalidTokenException();
		}
	}

	// 토큰 재발급: AT, RT 재발급
	@Transactional
	public TokenDto reissue(String requestRefreshTokenInHeader) {

		Authentication authentication = jwtProvider.getAuthenticationByRefreshToken(requestRefreshTokenInHeader);
		String principal = getPrincipalByRefreshToken(requestRefreshTokenInHeader);

		String refreshTokenInRedis = redisService.getValues("RT(" + SERVER + "):" + principal);
		if (refreshTokenInRedis == null) { // Redis에 저장되어 있는 RT가 없을 경우
			return null; // -> 재로그인 요청
		}
		// RT의 유효성 검사
		if (!jwtProvider.validateRefreshToken(refreshTokenInRedis)) {
			redisService.deleteValues("RT(" + SERVER + "):" + principal); //삭제
			return null; // -> 재로그인 요청
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String authorities = getAuthorities(authentication);

		// 토큰 재발급 및 Redis 업데이트
		redisService.deleteValues("RT(" + SERVER + "):" + principal); // 기존 RT 삭제
		TokenDto tokenDto = jwtProvider.createToken(principal, authorities);
		saveRefreshToken(SERVER, principal, tokenDto.getRefreshToken());
		return tokenDto;
	}

	// RT를 Redis에 저장
	@Transactional
	public void saveRefreshToken(String provider, String principal, String refreshToken) {
		redisService.setValuesWithTimeout("RT(" + provider + "):" + principal, // key
			refreshToken, // value
			jwtProvider.getTokenExpirationTime(refreshToken)); // timeout(milliseconds)
	}

	// 권한 이름 가져오기
	public String getAuthorities(Authentication authentication) {
		return authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));
	}

	// AT로부터 principal 추출
	public String getPrincipal(String requestAccessToken) {
		return jwtProvider.getAuthentication(requestAccessToken).getName();
	}

	// RT로부터 principal 추출
	public String getPrincipalByRefreshToken(String requestRefreshToken) {
		return jwtProvider.getAuthenticationByRefreshToken(requestRefreshToken).getName();
	}

	// "Bearer {AT}"에서 {AT} 추출
	public String resolveToken(String requestAccessTokenInHeader) {
		if (requestAccessTokenInHeader != null && requestAccessTokenInHeader.startsWith("Bearer ")) {
			return requestAccessTokenInHeader.substring(7);
		}
		return null;
	}

	// 로그아웃
	@Transactional
	public void logout(String requestAccessTokenInHeader) {
		String requestAccessToken = resolveToken(requestAccessTokenInHeader);
		String principal = getPrincipal(requestAccessToken);

		// Redis에 저장되어 있는 RT 삭제
		String refreshTokenInRedis = redisService.getValues("RT(" + SERVER + "):" + principal);
		if (refreshTokenInRedis != null) {
			redisService.deleteValues("RT(" + SERVER + "):" + principal);
		}

		// Redis에 로그아웃 처리한 AT 저장
		long expiration = jwtProvider.getTokenExpirationTime(requestAccessToken) - new Date().getTime();
		redisService.setValuesWithTimeout(requestAccessToken,
			"logout",
			expiration);
	}
}
