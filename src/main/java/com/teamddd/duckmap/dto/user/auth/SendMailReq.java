package com.teamddd.duckmap.dto.user.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class SendMailReq {
	@NotBlank
	@Email
	private String email;
}
