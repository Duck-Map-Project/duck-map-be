package com.teamddd.duckmap.config.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.teamddd.duckmap.config.security.UserDetailsServiceImpl;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.entity.Role;
import com.teamddd.duckmap.repository.MemberRepository;

public class WithMockAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithMockAuthUser> {
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	UserDetailsServiceImpl securityUserDetailsService;

	@Override
	public SecurityContext createSecurityContext(WithMockAuthUser withAccount) {
		String email = withAccount.email();

		Member member = Member.builder().email(email).username("member1")
			.password("@Aaaa1234").role(Role.USER).build();
		memberRepository.save(member);

		UserDetails principal = securityUserDetailsService.loadUserByUsername(email);
		Authentication authentication = new UsernamePasswordAuthenticationToken(principal,
			principal.getPassword(),
			principal.getAuthorities());
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(authentication);

		return securityContext;
	}
}
