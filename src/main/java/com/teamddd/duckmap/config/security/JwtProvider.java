package com.teamddd.duckmap.config.security;

import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.teamddd.duckmap.entity.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtProvider {
	@Value("${jwt.secret}")
	private String secretKey;
	//access token 유효시간 30분
	private final long TOKEN_VALID_TIME = 30 * 60 * 1000L;
	private final long REFRESH_TOKEN_VALID_TIME = 60 * 60 * 24 * 7 * 1000L;
	private final SecurityUserDetailsService securityUserDetailsService;

	// 객체 초기화, secret Key를 Base64로 인코딩
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder()
			.encodeToString(secretKey.getBytes());
	}

	// JWT 토큰 생성
	public String createToken(String userPK, Role role) {
		Claims claims = Jwts.claims().setSubject(userPK); // JWT payload에 저장되는 정보 단위
		claims.put("role", role); // 정보 저장 (key-value)
		Date now = new Date();

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + TOKEN_VALID_TIME)) // set Expire Time
			.signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘과 signature에 들어갈 secret 값 세팅
			.compact();
	}

	// JWT 토큰에서 인증 정보 조회
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = securityUserDetailsService.loadUserByUsername(this.getUserPK(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	// 토큰에서 회원 정보 추출
	public String getUserPK(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	// Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN": "TOKEN 값"
	public String resolveToken(HttpServletRequest request) {
		return request.getHeader("X-AUTH-TOKEN");
	}

	// 토큰의 유효성 + 만료일자 확인
	public boolean validateToken(String jwtToken) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}
}
