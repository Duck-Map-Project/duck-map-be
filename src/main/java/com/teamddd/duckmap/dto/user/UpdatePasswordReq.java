package com.teamddd.duckmap.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Getter;

@Getter
public class UpdatePasswordReq {
	@NotBlank
	private String currentPassword;
	@NotBlank
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
		message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
	private String newPassword;
}
