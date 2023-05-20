package com.teamddd.duckmap.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class CreateUserReq {
	@NotBlank
	private String username;
	@NotBlank
	@Email
	private String email;
	@NotBlank
	private String password;
}
