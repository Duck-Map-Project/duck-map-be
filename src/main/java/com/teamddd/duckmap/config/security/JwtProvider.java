package com.teamddd.duckmap.config.security;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.service.RedisService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional(readOnly = true)
public class JwtProvider {
	@Value("${jwt.secret}")
	private String secretKey;
	private static Key signingKey;
	private static final String AUTHORITIES_KEY = "role";
	private static final String EMAIL_KEY = "email";
	//access token 유효시간 30분
	private final long ACCESS_TOKEN_VALID_TIME = 30 * 60 * 1000L;
	private final long REFRESH_TOKEN_VALID_TIME = 60 * 60 * 24 * 7 * 1000L;
	private final SecurityUserDetailsService userDetailsService;
	private final RedisService redisService;

	public JwtProvider(SecurityUserDetailsService userDetailsService, RedisService redisService) {
		this.userDetailsService = userDetailsService;
		this.redisService = redisService;
	}

	// 객체 초기화, secret Key를 Base64로 인코딩
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder()
			.encodeToString(secretKey.getBytes());
		signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	// JWT 토큰 생성
	public TokenDto createToken(String email, String authorities) {
		Long now = System.currentTimeMillis();

		String accessToken = Jwts.builder()
			.setHeaderParam("typ", "JWT")
			.setHeaderParam("alg", "HS512")
			.setExpiration(new Date(now + ACCESS_TOKEN_VALID_TIME))
			.setSubject("access-token")
			.claim(EMAIL_KEY, email)
			.claim(AUTHORITIES_KEY, authorities)
			.signWith(signingKey)
			.compact();

		String refreshToken = Jwts.builder()
			.setHeaderParam("typ", "JWT")
			.setHeaderParam("alg", "HS512")
			.setExpiration(new Date(now + REFRESH_TOKEN_VALID_TIME))
			.setSubject("refresh-token")
			.signWith(signingKey)
			.compact();

		return new TokenDto(accessToken, refreshToken);

	}

	// 토큰으로부터 정보 추출
	public Claims getClaims(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(signingKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) { // Access Token
			return e.getClaims();
		}
	}

	// JWT 토큰에서 인증 정보 조회
	public Authentication getAuthentication(String token) {
		String email = getClaims(token).get(EMAIL_KEY).toString();
		UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(email);
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public long getTokenExpirationTime(String token) {
		return getClaims(token).getExpiration().getTime();
	}

	// 토큰의 유효성 + 만료일자 확인
	public boolean validateRefreshToken(String refreshToken) {
		try {
			if (redisService.getValues(refreshToken).equals("delete")) { // 회원 탈퇴했을 경우
				return false;
			}
			Jwts.parserBuilder()
				.setSigningKey(signingKey)
				.build()
				.parseClaimsJws(refreshToken);
			return true;
		} catch (SignatureException e) {
			log.error("Invalid JWT signature.");
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token.");
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token.");
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token.");
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty.");
		} catch (NullPointerException e) {
			log.error("JWT Token is empty.");
		}
		return false;
	}

	// Filter에서 사용
	public boolean validateAccessToken(String accessToken) {
		String redisServiceValues = redisService.getValues(accessToken);
		try {
			if (redisServiceValues != null // NPE 방지
				&& redisServiceValues.equals("logout")) { // 로그아웃 했을 경우
				return false;
			}
			Jwts.parserBuilder()
				.setSigningKey(signingKey)
				.build()
				.parseClaimsJws(accessToken);
			return true;
		} catch (ExpiredJwtException e) {
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 재발급 검증 API에서 사용
	public boolean validateAccessTokenOnlyExpired(String accessToken) {
		try {
			return getClaims(accessToken)
				.getExpiration()
				.before(new Date());
		} catch (ExpiredJwtException e) {
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
