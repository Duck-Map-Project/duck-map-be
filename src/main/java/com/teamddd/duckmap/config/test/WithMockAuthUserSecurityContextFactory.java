package com.teamddd.duckmap.config.test;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithMockAuthUser> {

	@Override
	public SecurityContext createSecurityContext(WithMockAuthUser annotation) {

		final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

		final UsernamePasswordAuthenticationToken authenticationToken
			= new UsernamePasswordAuthenticationToken(annotation.username(), null,
			List.of(new SimpleGrantedAuthority(annotation.role())));
		securityContext.setAuthentication(authenticationToken);
		return securityContext;
	}
}
