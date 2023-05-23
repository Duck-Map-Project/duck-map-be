package com.teamddd.duckmap.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.dto.Result;
import com.teamddd.duckmap.dto.user.CreateUserReq;
import com.teamddd.duckmap.dto.user.CreateUserRes;
import com.teamddd.duckmap.dto.user.UpdatePasswordReq;
import com.teamddd.duckmap.dto.user.UpdateUserReq;
import com.teamddd.duckmap.dto.user.UserRes;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	@Operation(summary = "회원 가입")
	@PostMapping
	public Result<CreateUserRes> createUser(@Validated @RequestBody CreateUserReq createUserReq) {
		return Result.<CreateUserRes>builder()
			.data(
				CreateUserRes.builder()
					.id(1L)
					.build()
			)
			.build();
	}

	@Operation(summary = "회원 정보 조회", description = "로그인한 회원 정보 조회")
	@GetMapping("/me")
	public Result<UserRes> getUserInfo(HttpSession session) {
		return Result.<UserRes>builder()
			.data(UserRes.builder()
				.id(1L)
				.username("user1")
				.email("sample@naver.com")
				.userProfile(
					ImageRes.builder()
						.apiUrl("/images/")
						.filename("user1.jpg")
						.build()
				)
				.userType("USER")
				.loginAt(LocalDateTime.now())
				.build())
			.build();
	}

	@Operation(summary = "회원정보 수정", description = "로그인한 회원의 닉네임, 프로필 사진 변경 요청")
	@PutMapping("/me")
	public Result<Void> updateUser(HttpSession session, @Validated @RequestBody UpdateUserReq updateUserReq) {
		return Result.<Void>builder().build();
	}

	@Operation(summary = "비밀번호 변경", description = "로그인한 회원 비밀번호 변경")
	@PatchMapping("/me/password")
	public Result<Void> updatePassword(@Validated @RequestBody UpdatePasswordReq updatePasswordReq) {
		return Result.<Void>builder().build();
	}

	@Operation(summary = "회원 탈퇴")
	@DeleteMapping("/me")
	public Result<Void> deleteUser(HttpSession session) {
		return Result.<Void>builder().build();
	}

}
