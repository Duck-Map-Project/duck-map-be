package com.teamddd.duckmap.controller;

import javax.servlet.http.HttpSession;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamddd.duckmap.config.security.JwtTokenProvider;
import com.teamddd.duckmap.dto.Result;
import com.teamddd.duckmap.dto.user.auth.LoginUserReq;
import com.teamddd.duckmap.dto.user.auth.LoginUserRes;
import com.teamddd.duckmap.entity.LastSearchArtist;
import com.teamddd.duckmap.entity.User;
import com.teamddd.duckmap.repository.LastSearchArtistRepository;
import com.teamddd.duckmap.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	private final LastSearchArtistRepository lastSearchArtistRepository;

	@Operation(summary = "로그인")
	@PostMapping("/login")
	public Result<LoginUserRes> login(@Validated @RequestBody LoginUserReq loginUserRQ) {
		Long lastSearchArtistId;
		User user = userRepository.findByEmail(loginUserRQ.getEmail())
			.orElseThrow(() -> new IllegalArgumentException("가입 되지 않은 이메일입니다."));
		if (!passwordEncoder.matches(loginUserRQ.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("이메일 또는 비밀번호가 맞지 않습니다.");
		}

		LastSearchArtist lastSearchArtist = lastSearchArtistRepository.findByUserId(user.getId());

		if (lastSearchArtist != null) {
			lastSearchArtistId = lastSearchArtist.getId();
		} else {
			lastSearchArtistId = null;
		}

		String token = jwtTokenProvider.createToken(user.getEmail(), user.getUserType());

		return Result.<LoginUserRes>builder()
			.data(
				LoginUserRes.builder()
					.id(user.getId())
					.username(user.getUsername())
					.image(user.getImage())
					.lastSearchArtist(lastSearchArtistId)
					.token(token)
					.build()
			)
			.build();
	}

	@Operation(summary = "로그아웃")
	@GetMapping("/logout")
	public Result<Void> logout(HttpSession session) {
		return Result.<Void>builder()
			.build();
	}
}

