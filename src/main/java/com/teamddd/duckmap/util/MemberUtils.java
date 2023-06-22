package com.teamddd.duckmap.util;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.teamddd.duckmap.config.security.UserDetailsImpl;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.exception.AuthenticationRequiredException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberUtils {

	public static UserDetailsImpl getAuthMember() throws AuthenticationRequiredException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof UserDetailsImpl)) {
			throw new AuthenticationRequiredException();
		}
		return (UserDetailsImpl)auth.getPrincipal();
	}

	public static Optional<Member> getMember() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		if (principal instanceof UserDetailsImpl) {
			return Optional.of(((UserDetailsImpl)principal).getUser());
		}
		return Optional.empty();
	}
}
