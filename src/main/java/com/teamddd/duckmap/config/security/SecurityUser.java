package com.teamddd.duckmap.config.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class SecurityUser extends User {
	private final com.teamddd.duckmap.entity.User user;

	public SecurityUser(com.teamddd.duckmap.entity.User user) {
		super(user.getId().toString(), user.getPassword(),
			AuthorityUtils.createAuthorityList(user.getUserType().toString()));
		this.user = user;
	}

	public com.teamddd.duckmap.entity.User getUser() {
		return user;
	}
}
