package com.teamddd.duckmap.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.config.security.JwtProvider;
import com.teamddd.duckmap.dto.user.CreateMemberReq;
import com.teamddd.duckmap.dto.user.auth.LoginReq;
import com.teamddd.duckmap.entity.LastSearchArtist;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.entity.Role;
import com.teamddd.duckmap.repository.LastSearchArtistRepository;
import com.teamddd.duckmap.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final LastSearchArtistRepository lastSearchArtistRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	public Long join(CreateMemberReq createMemberReq) {
		return memberRepository.save(Member.builder()
			.email(createMemberReq.getEmail())
			.role(Role.USER)
			.username(createMemberReq.getUsername())
			.password(passwordEncoder.encode(createMemberReq.getPassword()))
			.build()).getId();
	}

	public Member findOne(LoginReq loginUserRQ) {
		Member member = memberRepository.findByEmail(loginUserRQ.getEmail())
			.orElseThrow(() -> new IllegalArgumentException("가입 되지 않은 이메일입니다."));
		if (!passwordEncoder.matches(loginUserRQ.getPassword(), member.getPassword())) {
			throw new IllegalArgumentException("이메일 또는 비밀번호가 맞지 않습니다.");
		}
		return member;
	}

	public String login(Member member) {
		return jwtProvider.createToken(member.getEmail(), member.getRole());
	}

	public Long findLastSearchArtist(Long memberId) {
		Optional<LastSearchArtist> optional = lastSearchArtistRepository.findByMemberId(memberId);

		return optional.map(LastSearchArtist::getId).orElse(null);
	}

}
