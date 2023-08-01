package com.teamddd.duckmap.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.teamddd.duckmap.common.Props;
import com.teamddd.duckmap.dto.user.CreateMemberReq;
import com.teamddd.duckmap.dto.user.MemberRes;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.entity.Role;
import com.teamddd.duckmap.exception.DuplicateEmailException;
import com.teamddd.duckmap.exception.DuplicateUsernameException;
import com.teamddd.duckmap.exception.InvalidPasswordException;
import com.teamddd.duckmap.exception.InvalidUuidException;
import com.teamddd.duckmap.exception.NonExistentMemberException;
import com.teamddd.duckmap.repository.MemberRepository;
import com.teamddd.duckmap.util.FileUtils;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
	private final Props props;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final RedisService redisService;

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

	public void checkMemberByEmail(String email) {
		memberRepository.findByEmail(email).orElseThrow(NonExistentMemberException::new);
	}

	public MemberRes getMyInfoBySecurity(String email) {
		return memberRepository.findByEmail(email)
			.map(MemberRes::of)
			.orElseThrow(NonExistentMemberException::new);
	}

	@Transactional
	public void updateMemberInfo(String email, String username, String image) {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(NonExistentMemberException::new);

		//닉네임이 변경됐다면 닉네임 중복 체크
		if (!member.getUsername().equals(username)) {
			checkDuplicateUsername(username);
		}

		//기존 프로필 image
		String oldImage = member.getImage();

		member.updateMemberInfo(username, image);

		//프로필 image가 변경되었다면 서버에서 기존 프로필 image 삭제
		if (StringUtils.hasText(oldImage) && !oldImage.equals(image)) {
			FileUtils.deleteFile(props.getImageDir(), oldImage);
		}
	}

	@Transactional
	public void updatePassword(String email, String currentPassword, String newPassword) {
		Member member = validatePassword(email, currentPassword);
		member.updatePassword(passwordEncoder.encode((newPassword)));
	}

	@Transactional
	public void deleteMember(Long id) {
		Member member = memberRepository.findById(id)
			.orElseThrow(NonExistentMemberException::new);

		//서버에서 프로필 이미지 삭제
		if (StringUtils.hasText(member.getImage())) {
			FileUtils.deleteFile(props.getImageDir(), member.getImage());
		}

		memberRepository.deleteById(id);
	}

	public Member validatePassword(String email, String password) {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(NonExistentMemberException::new);
		if (!passwordEncoder.matches(password, member.getPassword())) {
			throw new InvalidPasswordException();
		}
		return member;
	}

	@Transactional
	public void resetPassword(String uuid, String newPassword) {
		//redis에 uuid가 있는지 확인, 없으면 error
		String email = redisService.getValues(uuid);
		if (email == null) {
			throw new InvalidUuidException();
		}

		//redis에서 uuid로 email을 찾아온다.
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(NonExistentMemberException::new);

		//비밀번호 재설정
		member.updatePassword(passwordEncoder.encode((newPassword)));

		//비밀번호 업데이트 후 redis에서 uuid를 지운다.
		redisService.deleteValues(uuid);
	}

}
