package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.config.security.TokenDto;
import com.teamddd.duckmap.dto.user.CreateMemberReq;
import com.teamddd.duckmap.dto.user.auth.LoginReq;
import com.teamddd.duckmap.util.MemberUtils;

@SpringBootTest
@Transactional
public class AuthServiceTest {
	@Autowired
	AuthService authService;
	@Autowired
	MemberService memberService;

	@DisplayName("로그인해서 토큰으로 회원 정보 가져오기")
	@Test
	void loginAndToken() throws Exception {
		//given
		CreateMemberReq request = new CreateMemberReq();
		ReflectionTestUtils.setField(request, "username", "user1");
		ReflectionTestUtils.setField(request, "email", "string@string.com");
		ReflectionTestUtils.setField(request, "password", "@Alaa1234523");
		Long memberId = memberService.join(request);

		LoginReq loginReq = new LoginReq();
		ReflectionTestUtils.setField(loginReq, "email", "string@string.com");
		ReflectionTestUtils.setField(loginReq, "password", "@Alaa1234523");
		//when
		TokenDto tokenDto = authService.login(loginReq);

		//then
		String email = MemberUtils.getAuthMember().getUsername();
		assertThat(email).isEqualTo("string@string.com");
	}
}
