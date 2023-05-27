package com.teamddd.duckmap.repository;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.ArtistType;

@Transactional
@SpringBootTest
class ArtistRepositoryTest {
	@Autowired
	ArtistRepository artistRepository;
	@Autowired
	EntityManager em;

	private ArtistType createArtistType(String type) {
		return ArtistType.builder().type(type).build();
	}

	private Artist createArtist(String name, ArtistType type1, Artist group) {
		return Artist.builder()
			.name(name)
			.artistType(type1)
			.group(group)
			.build();
	}

	@Nested
	@DisplayName("artistType, name으로 artist 목록 조회")
	class FindByTypeAndName {
		@DisplayName("type, name을 null로 검색한 경우")
		@Test
		void findByTypeAndName1() throws Exception {
			//given
			ArtistType type1 = createArtistType("그룹");
			ArtistType type2 = createArtistType("아이돌");
			ArtistType type3 = createArtistType("모델");
			em.persist(type1);
			em.persist(type2);
			em.persist(type3);

			Artist group1 = createArtist("group1", type1, null);
			Artist artist2 = createArtist("artist2", type2, group1);
			Artist artist3 = createArtist("artist3", type2, group1);
			Artist group2 = createArtist("group2", type1, null);
			Artist artist4 = createArtist("artist4", type2, group2);
			Artist artist5 = createArtist("artist5", type3, null);
			Artist artist6 = createArtist("artist6", type3, null);
			em.persist(group1);
			em.persist(group2);
			em.persist(artist2);
			em.persist(artist3);
			em.persist(artist4);
			em.persist(artist5);
			em.persist(artist6);

			PageRequest request = PageRequest.of(0, 3);

			//when
			Page<Artist> findArtists = artistRepository.findByTypeAndName(null, null, request);

			//then
			assertThat(findArtists).hasSize(3)
				.extracting("name")
				.containsExactlyInAnyOrder("group1", "group2", "artist2");

			assertThat(findArtists.getTotalElements()).isEqualTo(7);
			assertThat(findArtists.getTotalPages()).isEqualTo(3);
		}

		@DisplayName("name으로만 검색한 경우, group name으로도 검색 가능")
		@Test
		void findByTypeAndName2() throws Exception {
			//given
			ArtistType type1 = createArtistType("그룹");
			ArtistType type2 = createArtistType("아이돌");
			ArtistType type3 = createArtistType("모델");
			em.persist(type1);
			em.persist(type2);
			em.persist(type3);

			Artist group1 = createArtist("group1", type1, null);
			Artist artist2 = createArtist("artist2", type2, group1);
			Artist artist3 = createArtist("artist3", type2, group1);
			Artist group2 = createArtist("group2", type1, null);
			Artist artist4 = createArtist("artist4", type2, group2);
			Artist artist5 = createArtist("artist5", type3, null);
			Artist artist6 = createArtist("artist6", type3, null);
			em.persist(group1);
			em.persist(group2);
			em.persist(artist2);
			em.persist(artist3);
			em.persist(artist4);
			em.persist(artist5);
			em.persist(artist6);

			PageRequest request = PageRequest.of(0, 3);

			//when
			Page<Artist> findArtists = artistRepository.findByTypeAndName(null, "2", request);

			//then
			assertThat(findArtists).hasSize(3)
				.extracting("name", "group.name")
				.containsExactlyInAnyOrder(Tuple.tuple("group2", null), Tuple.tuple("artist2", "group1"),
					Tuple.tuple("artist4", "group2"));

			assertThat(findArtists.getTotalElements()).isEqualTo(3);
			assertThat(findArtists.getTotalPages()).isEqualTo(1);
		}

		@DisplayName("type으로만 검색한 경우")
		@Test
		void findByTypeAndName3() throws Exception {
			//given
			ArtistType type1 = createArtistType("그룹");
			ArtistType type2 = createArtistType("아이돌");
			ArtistType type3 = createArtistType("모델");
			em.persist(type1);
			em.persist(type2);
			em.persist(type3);

			Artist group1 = createArtist("group1", type1, null);
			Artist artist2 = createArtist("artist2", type2, group1);
			Artist artist3 = createArtist("artist3", type2, group1);
			Artist group2 = createArtist("group2", type1, null);
			Artist artist4 = createArtist("artist4", type2, group2);
			Artist artist5 = createArtist("artist5", type3, null);
			Artist artist6 = createArtist("artist6", type3, null);
			em.persist(group1);
			em.persist(group2);
			em.persist(artist2);
			em.persist(artist3);
			em.persist(artist4);
			em.persist(artist5);
			em.persist(artist6);

			PageRequest request = PageRequest.of(0, 2);

			//when
			Page<Artist> findArtists = artistRepository.findByTypeAndName(type2.getId(), null, request);

			//then
			assertThat(findArtists).hasSize(2)
				.extracting("name")
				.containsExactlyInAnyOrder("artist2", "artist3");

			assertThat(findArtists.getTotalElements()).isEqualTo(3);
			assertThat(findArtists.getTotalPages()).isEqualTo(2);
		}

		@DisplayName("type, name으로 검색한 경우")
		@Test
		void findByTypeAndName4() throws Exception {
			//given
			ArtistType type1 = createArtistType("그룹");
			ArtistType type2 = createArtistType("아이돌");
			ArtistType type3 = createArtistType("모델");
			em.persist(type1);
			em.persist(type2);
			em.persist(type3);

			Artist group1 = createArtist("group1", type1, null);
			Artist artist2 = createArtist("artist2", type2, group1);
			Artist artist3 = createArtist("artist3", type2, group1);
			Artist group2 = createArtist("group2", type1, null);
			Artist artist4 = createArtist("artist4", type2, group2);
			Artist artist5 = createArtist("artist5", type3, null);
			Artist artist6 = createArtist("artist6", type3, null);
			em.persist(group1);
			em.persist(group2);
			em.persist(artist2);
			em.persist(artist3);
			em.persist(artist4);
			em.persist(artist5);
			em.persist(artist6);

			PageRequest request = PageRequest.of(0, 2);

			//when
			Page<Artist> findArtists = artistRepository.findByTypeAndName(type2.getId(), "1", request);

			//then
			assertThat(findArtists).hasSize(2)
				.extracting("name", "group.name")
				.containsExactlyInAnyOrder(
					Tuple.tuple("artist2", "group1"),
					Tuple.tuple("artist3", "group1")
				);

			assertThat(findArtists.getTotalElements()).isEqualTo(2);
			assertThat(findArtists.getTotalPages()).isEqualTo(1);
		}

	}
}