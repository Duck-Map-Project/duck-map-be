package com.teamddd.duckmap.dto.user.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SendResetPasswordEmailRes {
	private String UUID;
}
