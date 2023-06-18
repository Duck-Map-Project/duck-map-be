package com.teamddd.duckmap.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamddd.duckmap.dto.user.CreateMemberReq;
import com.teamddd.duckmap.dto.user.CreateMemberRes;
import com.teamddd.duckmap.dto.user.DeleteMemberReq;
import com.teamddd.duckmap.dto.user.MemberRes;
import com.teamddd.duckmap.dto.user.ResetPasswordReq;
import com.teamddd.duckmap.dto.user.UpdateMemberReq;
import com.teamddd.duckmap.dto.user.UpdatePasswordReq;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.service.AuthService;
import com.teamddd.duckmap.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
	private final MemberService memberService;
	private final AuthService authService;

	@Operation(summary = "회원 가입")
	@PostMapping("/join")
	public CreateMemberRes createMember(@Validated @RequestBody CreateMemberReq createMemberReq) {
		Long id = memberService.join(createMemberReq);
		return CreateMemberRes.builder()
			.id(id)
			.build();
	}

	@Operation(summary = "회원 정보 조회", description = "로그인한 회원 정보 조회")
	@GetMapping("/me")
	public MemberRes getMemberInfo() {
		return memberService.getMyInfoBySecurity();
	}

	@Operation(summary = "회원정보 수정", description = "로그인한 회원의 닉네임, 프로필 사진 변경 요청")
	@PutMapping("/me")
	public void updateMember(@Validated @RequestBody UpdateMemberReq updateMemberReq) {
		memberService.updateMemberInfo(updateMemberReq.getUsername(), updateMemberReq.getImage());
	}

	@Operation(summary = "비밀번호 변경", description = "로그인한 회원 비밀번호 변경")
	@PatchMapping("/me/password")
	public void updatePassword(@Validated @RequestBody UpdatePasswordReq updatePasswordReq) {
		memberService.updatePassword(updatePasswordReq.getCurrentPassword(), updatePasswordReq.getNewPassword());
	}

	@Operation(summary = "회원 탈퇴")
	@DeleteMapping("/me")
	public void deleteMember(@RequestHeader("Authorization") String requestAccessToken,
		@Validated @RequestBody DeleteMemberReq deleteMemberReq) {
		Member member = memberService.validatePassword(deleteMemberReq.getPassword());
		authService.logout(requestAccessToken);
		memberService.deleteMember(member.getId());
	}

	@Operation(summary = "비밀번호 재설정", description = "비밀번호를 새로 재설정")
	@PatchMapping("/reset-password/{id}")
	public void resetPassword(@PathVariable("id") String uuid,
		@Validated @RequestBody ResetPasswordReq resetPasswordReq) {
		memberService.resetPassword(uuid, resetPasswordReq.getNewPassword());
	}

}
