package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.user.CreateMemberReq;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.exception.NonExistentMemberException;
import com.teamddd.duckmap.repository.MemberRepository;

@SpringBootTest
@Transactional
public class MemberServiceTest {

	@Autowired
	MemberService memberService;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	EntityManager em;
	@Mock
	private PasswordEncoder passwordEncoder;

	@DisplayName("생성된 회원 확인")
	@Test
	void createMember() throws Exception {
		//given
		CreateMemberReq request = new CreateMemberReq();
		ReflectionTestUtils.setField(request, "username", "user1");
		ReflectionTestUtils.setField(request, "email", "string@string.com");
		ReflectionTestUtils.setField(request, "password", "@Aaaa1234523");
		//when
		Long memberId = memberService.join(request);
		//then
		assertThat(memberId).isNotNull();
		Optional<Member> findMember = memberRepository.findById(memberId);
		assertThat(findMember.get())
			.extracting("username")
			.isEqualTo("user1");
	}

	@DisplayName("회원 탈퇴 확인")
	@Test
	void deleteMember() throws Exception {
		//given
		Member member = Member.builder()
			.username("member1")
			.build();
		em.persist(member);

		//when
		memberService.deleteMember(member.getId());
		//then
		Optional<Member> findMember = memberRepository.findById(member.getId());
		assertThat(findMember).isEmpty();
	}

	@DisplayName("잘못된 이메일로 Member를 찾을 수 없다.")
	@Test
	void checkMemberByEmail() throws Exception {
		//given
		String email = "sample@email.com";

		//when //then
		assertThatThrownBy(() -> memberService.checkMemberByEmail(email))
			.isInstanceOf(NonExistentMemberException.class)
			.hasMessage("잘못된 사용자 정보입니다");
	}

	@DisplayName("회원의 비밀번호 검증")
	@Test
	void validatePassword() throws Exception {
		//given
		String password = passwordEncoder.encode("@Aaaa1234");
		Member member = Member.builder()
			.email("email@email.com")
			.password(password)
			.build();
		em.persist(member);
		when(passwordEncoder.matches(password, member.getPassword())).thenReturn(true);
		//when
		Member findMember = memberService.validatePassword(member.getEmail(), member.getPassword());
		//then
		assertThat(findMember).isNotNull();
		assertThat(findMember).extracting("email").isEqualTo(member.getEmail());
	}

}
