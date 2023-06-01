package com.teamddd.duckmap.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.User;

@SpringBootTest
@Transactional
public class UserRepositoryTest {
	@Autowired
	UserRepository userRepository;
	@Autowired
	EntityManager em;

	@Test
	void findByEmail() throws Exception {
		//given
		User user1 = User.builder().username("user1").email("user1@email.com").build();
		User user2 = User.builder().username("user2").email("user2@email.com").build();
		User user3 = User.builder().username("user3").email("user3@email.com").build();
		em.persist(user1);
		em.persist(user2);
		em.persist(user3);

		//when
		Optional<User> optionalUser = userRepository.findByEmail(user1.getEmail());
		User findUser = optionalUser.get();
		//then
		assertThat(findUser.getEmail()).isEqualTo("user1@email.com");
		assertThat(findUser.getUsername()).isEqualTo("user1");
	}

	@Test
	void findByUsername() throws Exception {
		//given
		User user1 = User.builder().username("user1").email("user1@email.com").build();
		User user2 = User.builder().username("user2").email("user2@email.com").build();
		User user3 = User.builder().username("user3").email("user3@email.com").build();
		em.persist(user1);
		em.persist(user2);
		em.persist(user3);

		//when
		Optional<User> optionalUser = userRepository.findByUsername(user2.getUsername());
		User findUser = optionalUser.get();
		//then
		assertThat(findUser.getEmail()).isEqualTo("user2@email.com");
		assertThat(findUser.getUsername()).isEqualTo("user2");
	}
}
