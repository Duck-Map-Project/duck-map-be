package com.teamddd.duckmap.dto.user;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class UpdatePasswordReq {
	@NotBlank
	private String currentPassword;
	@NotBlank
	private String newPassword;
}
