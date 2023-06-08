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
		return memberRepository.findByEmail(MemberUtils.getAuthMember().getUsername())
			.map(MemberRes::of)
			.orElseThrow(InvalidMemberException::new);
	}

	@Transactional
	public void updateMemberInfo(String username, String image) {
		Member member = memberRepository.findByEmail(MemberUtils.getAuthMember().getUsername())
			.orElseThrow(InvalidMemberException::new);
		member.updateMemberInfo(username, image);

	}

	@Transactional
	public void updatePassword(String currentPassword, String newPassword) {
		Member member = memberRepository.findByEmail(MemberUtils.getAuthMember().getUsername())
			.orElseThrow(InvalidMemberException::new);
		if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
			throw new RuntimeException("비밀번호가 맞지 않습니다");
		}
		member.updatePassword(passwordEncoder.encode((newPassword)));
	}

	@Transactional
	public void deleteMember() {
		Member member = memberRepository.findByEmail(MemberUtils.getAuthMember().getUsername())
			.orElseThrow(InvalidMemberException::new);
		memberRepository.delete(member);
	}

}
