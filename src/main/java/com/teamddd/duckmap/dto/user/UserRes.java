package com.teamddd.duckmap.dto.user;

import java.time.LocalDateTime;

import com.teamddd.duckmap.dto.ImageRes;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRes {
	private Long id;
	private String username;
	private String email;
	private ImageRes userProfile;
	private LocalDateTime loginAt;
}
