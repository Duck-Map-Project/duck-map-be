package com.teamddd.duckmap.config.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.teamddd.duckmap.config.security.UserDetailsImpl;

public class AuditorAwareImpl implements AuditorAware<String> {
	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
			|| !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
			return Optional.empty();
		}

		return Optional.of(((UserDetailsImpl)authentication.getPrincipal()).getUser().getUsername());
	}
}
