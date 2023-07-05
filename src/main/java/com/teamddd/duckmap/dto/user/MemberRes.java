package com.teamddd.duckmap.dto.user;

import java.time.LocalDateTime;

import com.teamddd.duckmap.common.ApiUrl;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.entity.Role;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberRes {
	private Long id;
	private String username;
	private String email;
	private String userProfile;
	private Role role;
	private LocalDateTime loginAt;

	public static MemberRes of(Member member) {
		return MemberRes.builder()
			.id(member.getId())
			.username(member.getUsername())
			.email(member.getEmail())
			.userProfile(ApiUrl.IMAGE + member.getImage())
			.role(member.getRole())
			.loginAt(member.getLoginAt())
			.build();
	}
}
