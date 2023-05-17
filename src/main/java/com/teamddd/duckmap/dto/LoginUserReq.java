package com.teamddd.duckmap.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserReq {
	@NotBlank
	private String email;
	@NotBlank
	private String password;
}
