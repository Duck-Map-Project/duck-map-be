package com.teamddd.duckmap.dto.user.auth;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class CheckVerifyCodeReq {
	@NotBlank
	private String verifyCode;
	@NotBlank
	private String userInputVerifyCode;
}
