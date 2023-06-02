package com.teamddd.duckmap.config.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.repository.MemberRepository;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
	@Autowired
	private MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Member> optional = memberRepository.findByEmail(username);
		if (optional.isEmpty()) {
			throw new UsernameNotFoundException(username + " 사용자 찾을 수 없음");
		} else {
			Member member = optional.get();
			return new SecurityUser(member);
		}

	}
}
