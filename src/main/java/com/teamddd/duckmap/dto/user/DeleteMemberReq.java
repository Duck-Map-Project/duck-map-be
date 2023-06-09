package com.teamddd.duckmap.dto.user;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class DeleteMemberReq {
	@NotBlank
	private String password;
}
