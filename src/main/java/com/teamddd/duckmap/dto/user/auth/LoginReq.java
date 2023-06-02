package com.teamddd.duckmap.dto.user.auth;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginReq {
	@NotBlank
	private String email;
	@NotBlank
	private String password;
}
