package com.teamddd.duckmap.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.user.CreateMemberReq;
import com.teamddd.duckmap.dto.user.MemberRes;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.entity.Role;
import com.teamddd.duckmap.exception.DuplicateEmailException;
import com.teamddd.duckmap.exception.DuplicateUsernameException;
import com.teamddd.duckmap.exception.InvalidMemberException;
import com.teamddd.duckmap.repository.MemberRepository;
import com.teamddd.duckmap.util.MemberUtils;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
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

	public MemberRes getMyInfoBySecurity() {
		return memberRepository.findById(MemberUtils.getAuthMember().getId())
			.map(MemberRes::of)
			.orElseThrow(InvalidMemberException::new);
	}

	@Transactional
	public void updateUsername(String username) {
		Member member = memberRepository.findByEmail(MemberUtils.getAuthMember().getEmail())
			.orElseThrow(InvalidMemberException::new);
		member.updateUsername(username);
		memberRepository.save(member);
	}

	@Transactional
	public void updatePassword(String currentPassword, String newPassword) {
		Member member = memberRepository.findById(MemberUtils.getAuthMember().getId())
			.orElseThrow(InvalidMemberException::new);
		if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
			throw new RuntimeException("비밀번호가 맞지 않습니다");
		}
		member.updatePassword(passwordEncoder.encode((newPassword)));
		memberRepository.save(member);
	}
}
