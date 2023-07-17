package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.LastSearchArtist;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.repository.LastSearchArtistRepository;

@Transactional
@SpringBootTest
class LastSearchArtistServiceTest {
	@Autowired
	EntityManager em;
	@Autowired
	LastSearchArtistService lastSearchArtistService;
	@Autowired
	LastSearchArtistRepository lastSearchArtistRepository;

	Member createMember(String email) {
		return Member.builder()
			.email(email)
			.build();
	}

	Artist createArtist(String name) {
		return Artist.builder()
			.name(name)
			.build();
	}

	LastSearchArtist createLastSearchArtist(Member member, Artist artist) {
		return LastSearchArtist.builder()
			.member(member)
			.artist(artist)
			.build();
	}

	@DisplayName("마지막 검색 아티스트를 저장한다")
	@Nested
	class SaveLastSearchArtist {
		@DisplayName("기존에 검색한 기록이 없는 경우")
		@Test
		void saveLastSearchArtist1() throws Exception {
			//given
			Member member1 = createMember("member1");
			em.persist(member1);

			Artist artist1 = createArtist("artist1");
			em.persist(artist1);

			//when
			lastSearchArtistService.saveLastSearchArtist(member1, artist1);

			//then
			Optional<LastSearchArtist> findLastSearch = lastSearchArtistRepository.findByMember(member1);

			assertThat(findLastSearch).isNotEmpty();
			assertThat(findLastSearch.get().getId()).isNotNull();
			assertThat(findLastSearch.get()).extracting("member.email", "artist.name")
				.containsExactly("member1", "artist1");
		}

		@DisplayName("기존에 검색한 기록이 있는 경우")
		@Test
		void saveLastSearchArtist2() throws Exception {
			//given
			Member member1 = createMember("member1");
			em.persist(member1);

			Artist beforeArtist = createArtist("before");
			Artist afterArtist = createArtist("after");
			em.persist(beforeArtist);
			em.persist(afterArtist);

			LastSearchArtist lastSearchArtist1 = createLastSearchArtist(member1, beforeArtist);
			em.persist(lastSearchArtist1);

			//when
			lastSearchArtistService.saveLastSearchArtist(member1, afterArtist);

			//then
			Optional<LastSearchArtist> findLastSearch = lastSearchArtistRepository.findByMember(member1);

			assertThat(findLastSearch).isNotEmpty();
			assertThat(findLastSearch.get()).extracting("member.email", "artist.name")
				.containsExactly("member1", "after");
		}
	}
}
