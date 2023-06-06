package com.teamddd.duckmap.config.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityRule {
	public static final String HAS_ROLE_ADMIN = "hasRole('ADMIN')";
}
