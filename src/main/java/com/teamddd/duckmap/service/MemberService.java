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
import com.teamddd.duckmap.exception.DuplicateEmailException;
import com.teamddd.duckmap.exception.DuplicateUsernameException;
import com.teamddd.duckmap.exception.InvalidMemberException;
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
		checkDuplicateEmail(createMemberReq.getEmail());
		checkDuplicateUsername(createMemberReq.getUsername());

		return memberRepository.save(Member.builder()
			.email(createMemberReq.getEmail())
			.role(Role.USER)
			.username(createMemberReq.getUsername())
			.password(passwordEncoder.encode(createMemberReq.getPassword()))
			.build()).getId();
	}

	private void checkDuplicateEmail(String email) {
		memberRepository.findByEmail(email).ifPresent(member -> {
			throw new DuplicateEmailException();
		});

	}

	private void checkDuplicateUsername(String username) {
		memberRepository.findByUsername(username).ifPresent(member -> {
			throw new DuplicateUsernameException();
		});
	}

	public Member findOne(LoginReq loginUserRQ) {
		Member member = memberRepository.findByEmail(loginUserRQ.getEmail())
			.orElseThrow(InvalidMemberException::new);
		if (!passwordEncoder.matches(loginUserRQ.getPassword(), member.getPassword())) {
			throw new InvalidMemberException();
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
