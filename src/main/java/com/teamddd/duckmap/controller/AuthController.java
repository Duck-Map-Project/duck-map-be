package com.teamddd.duckmap.controller;

import javax.servlet.http.HttpSession;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamddd.duckmap.dto.Result;
import com.teamddd.duckmap.dto.user.auth.LoginUserReq;
import com.teamddd.duckmap.dto.user.auth.LoginUserRes;
import com.teamddd.duckmap.entity.User;
import com.teamddd.duckmap.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	private final MemberService memberService;

	@Operation(summary = "로그인")
	@PostMapping("/login")
	public Result<LoginUserRes> login(@Validated @RequestBody LoginUserReq loginUserRQ) {
		User user = memberService.findOne(loginUserRQ);
		String token = memberService.login(user);
		Long lastSearchArtistId = memberService.findLastSearchArtist(user.getId());
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

