package com.teamddd.duckmap.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.teamddd.duckmap.exception.InvalidMemberException;
import com.teamddd.duckmap.exception.InvalidTokenException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		// Access Token 추출
		String accessToken = resolveToken(request);

		try { // 정상 토큰인지 검사
			if (accessToken != null && jwtProvider.validateAccessToken(accessToken)) {
				Authentication authentication = jwtProvider.getAuthentication(accessToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				log.debug("Save authentication in SecurityContextHolder.");
			}
		} catch (InvalidTokenException e) { // 잘못된 토큰일 경우
			SecurityContextHolder.clearContext();
			log.debug("Invalid JWT token.");
			response.sendError(403);
		} catch (InvalidMemberException e) { // 회원을 찾을 수 없을 경우
			SecurityContextHolder.clearContext();
			log.debug("Can't find user.");
			response.sendError(403);
		}
		filterChain.doFilter(request, response);
	}

	// HTTP Request 헤더로부터 토큰 추출
	public String resolveToken(HttpServletRequest httpServletRequest) {
		String bearerToken = httpServletRequest.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
