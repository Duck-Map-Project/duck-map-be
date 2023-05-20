package com.teamddd.duckmap.dto.user;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class UpdateUserReq {
	@NotBlank
	private String username;
	private String image;
}
