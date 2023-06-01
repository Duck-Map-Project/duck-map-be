package com.teamddd.duckmap.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.user.CreateUserReq;
import com.teamddd.duckmap.entity.User;
import com.teamddd.duckmap.entity.UserType;
import com.teamddd.duckmap.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true) //읽기 전용
@RequiredArgsConstructor
public class MemberService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public Long join(CreateUserReq createUserReq) {
		return userRepository.save(User.builder()
			.email(createUserReq.getEmail())
			.userType(UserType.USER)
			.username(createUserReq.getUsername())
			.password(passwordEncoder.encode(createUserReq.getPassword()))
			.build()).getId();
	}
}
