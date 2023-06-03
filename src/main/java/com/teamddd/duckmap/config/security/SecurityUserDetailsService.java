package com.teamddd.duckmap.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.exception.InvalidMemberException;
import com.teamddd.duckmap.repository.MemberRepository;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
	@Autowired
	private MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		Member member = memberRepository.findByEmail(username)
			.orElseThrow(InvalidMemberException::new);
		return new SecurityUser(member);
	}
}
