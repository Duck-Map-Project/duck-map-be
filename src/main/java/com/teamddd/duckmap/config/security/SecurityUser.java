package com.teamddd.duckmap.config.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import com.teamddd.duckmap.entity.Member;

public class SecurityUser extends User {
	private final Member member;

	public SecurityUser(Member member) {
		super(member.getId().toString(), member.getPassword(),
			AuthorityUtils.createAuthorityList(member.getRole().toString()));
		this.member = member;
	}

	public Member getUser() {
		return member;
	}
}
