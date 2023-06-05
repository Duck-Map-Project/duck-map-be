package com.teamddd.duckmap.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.config.security.JwtProvider;
import com.teamddd.duckmap.dto.user.auth.LoginReq;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.exception.InvalidMemberException;
import com.teamddd.duckmap.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	public Member login(LoginReq loginUserRQ) {
		Member member = memberRepository.findByEmail(loginUserRQ.getEmail())
			.orElseThrow(InvalidMemberException::new);
		if (!passwordEncoder.matches(loginUserRQ.getPassword(), member.getPassword())) {
			throw new InvalidMemberException();
		}
		return member;
	}

	public String createToken(Member member) {
		return jwtProvider.createToken(member.getEmail(), member.getRole());
	}
}
