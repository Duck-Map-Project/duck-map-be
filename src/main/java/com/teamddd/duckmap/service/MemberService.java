package com.teamddd.duckmap.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.config.security.JwtProvider;
import com.teamddd.duckmap.dto.user.CreateUserReq;
import com.teamddd.duckmap.dto.user.auth.LoginUserReq;
import com.teamddd.duckmap.entity.LastSearchArtist;
import com.teamddd.duckmap.entity.User;
import com.teamddd.duckmap.entity.UserType;
import com.teamddd.duckmap.repository.LastSearchArtistRepository;
import com.teamddd.duckmap.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true) //읽기 전용
@RequiredArgsConstructor
public class MemberService {
	private final UserRepository userRepository;
	private final LastSearchArtistRepository lastSearchArtistRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	public Long join(CreateUserReq createUserReq) {
		return userRepository.save(User.builder()
			.email(createUserReq.getEmail())
			.userType(UserType.USER)
			.username(createUserReq.getUsername())
			.password(passwordEncoder.encode(createUserReq.getPassword()))
			.build()).getId();
	}

	public User findOne(LoginUserReq loginUserRQ) {
		User user = userRepository.findByEmail(loginUserRQ.getEmail())
			.orElseThrow(() -> new IllegalArgumentException("가입 되지 않은 이메일입니다."));
		if (!passwordEncoder.matches(loginUserRQ.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("이메일 또는 비밀번호가 맞지 않습니다.");
		}
		return user;
	}

	public String login(User user) {
		return jwtProvider.createToken(user.getEmail(), user.getUserType());
	}

	public Long findLastSearchArtist(Long userId) {
		Optional<LastSearchArtist> optional = lastSearchArtistRepository.findByUserId(userId);

		return optional.map(LastSearchArtist::getId).orElse(null);
	}

}
