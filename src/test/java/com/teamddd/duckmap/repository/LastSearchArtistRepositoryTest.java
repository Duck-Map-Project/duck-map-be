package com.teamddd.duckmap.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.LastSearchArtist;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.entity.Role;

@Transactional
@SpringBootTest
class LastSearchArtistRepositoryTest {
	@Autowired
	LastSearchArtistRepository lastSearchArtistRepository;
	@Autowired
	EntityManager em;

	@DisplayName("Artist Id로 LastSearchArtist 목록 삭제")
	@Test
	void deleteByArtistId() throws Exception {
		//given
		Member member1 = createMember("member1");
		Member member2 = createMember("member2");
		em.persist(member1);
		em.persist(member2);

		Artist artist1 = createArtist("artist1");
		em.persist(artist1);

		LastSearchArtist lastSearchArtist1 = createLastSearchArtist(member1, artist1);
		LastSearchArtist lastSearchArtist2 = createLastSearchArtist(member2, artist1);
		em.persist(lastSearchArtist1);
		em.persist(lastSearchArtist2);

		//when
		int deleteCount = lastSearchArtistRepository.deleteByArtistId(artist1.getId());

		//then
		assertThat(deleteCount).isEqualTo(2);

		List<LastSearchArtist> findLastSearchArtists = lastSearchArtistRepository.findAll();
		assertThat(findLastSearchArtists).hasSize(0);
	}

	@DisplayName("Member Id로 LastSearchArtist 목록 삭제")
	@Test
	void deleteByMemberId() throws Exception {
		//given
		Member member1 = createMember("member1");
		em.persist(member1);

		Artist artist1 = createArtist("artist1");
		Artist artist2 = createArtist("artist2");
		Artist artist3 = createArtist("artist3");
		em.persist(artist1);
		em.persist(artist2);
		em.persist(artist3);

		LastSearchArtist lastSearchArtist1 = createLastSearchArtist(member1, artist1);
		LastSearchArtist lastSearchArtist2 = createLastSearchArtist(member1, artist2);
		LastSearchArtist lastSearchArtist3 = createLastSearchArtist(member1, artist3);
		em.persist(lastSearchArtist1);
		em.persist(lastSearchArtist2);
		em.persist(lastSearchArtist3);

		//when
		int deleteCount = lastSearchArtistRepository.deleteByMemberId(member1.getId());

		//then
		assertThat(deleteCount).isEqualTo(3);

		List<LastSearchArtist> findLastSearchArtists = lastSearchArtistRepository.findAll();
		assertThat(findLastSearchArtists).hasSize(0);
	}

	Member createMember(String email) {
		return Member.builder()
			.email(email)
			.username("username")
			.password("password")
			.role(Role.USER)
			.image("image")
			.build();
	}

	Artist createArtist(String name) {
		return Artist.builder()
			.group(null)
			.artistType(null)
			.image("image")
			.name(name)
			.build();
	}

	LastSearchArtist createLastSearchArtist(Member member, Artist artist) {
		return LastSearchArtist.builder()
			.member(member)
			.artist(artist)
			.build();
	}
}
