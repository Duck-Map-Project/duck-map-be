package com.teamddd.duckmap.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.Member;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	EntityManager em;

	@Test
	void findByEmail() throws Exception {
		//given
		Member member1 = Member.builder().username("user1").email("user1@email.com").build();
		Member member2 = Member.builder().username("user2").email("user2@email.com").build();
		Member member3 = Member.builder().username("user3").email("user3@email.com").build();
		em.persist(member1);
		em.persist(member2);
		em.persist(member3);

		//when
		Optional<Member> optionalUser = memberRepository.findByEmail(member1.getEmail());
		Member findMember = optionalUser.get();
		//then
		assertThat(findMember.getEmail()).isEqualTo("user1@email.com");
		assertThat(findMember.getUsername()).isEqualTo("user1");
	}

	@Test
	void findByUsername() throws Exception {
		//given
		Member member1 = Member.builder().username("user1").email("user1@email.com").build();
		Member member2 = Member.builder().username("user2").email("user2@email.com").build();
		Member member3 = Member.builder().username("user3").email("user3@email.com").build();
		em.persist(member1);
		em.persist(member2);
		em.persist(member3);

		//when
		Optional<Member> optionalUser = memberRepository.findByUsername(member2.getUsername());
		Member findMember = optionalUser.get();
		//then
		assertThat(findMember.getEmail()).isEqualTo("user2@email.com");
		assertThat(findMember.getUsername()).isEqualTo("user2");
	}
}
