package com.teamddd.duckmap.dto.user;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class CreateUserReq {
	@NotBlank
	private String username;
	@NotBlank
	private String email;
	@NotBlank
	private String password;
	private String salt;
	private String image;
	private LocalDateTime loginAt;
}
