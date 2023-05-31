package com.teamddd.duckmap.dto.user.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginUserRes {
	private Long id;
	private String username;
	private String image;
	private Long lastSearchArtist;
	private String token;
}
