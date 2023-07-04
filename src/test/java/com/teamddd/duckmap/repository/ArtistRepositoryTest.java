package com.teamddd.duckmap.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

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

	@DisplayName("group fk로 artist 목록 조회")
	@Test
	void findByGroup() throws Exception {
		//given
		ArtistType type1 = createArtistType("그룹");
		ArtistType type2 = createArtistType("아이돌");
		em.persist(type1);
		em.persist(type2);

		Artist group1 = createArtist("group1", type1, null);
		Artist artist2 = createArtist("artist2", type2, group1);
		Artist artist3 = createArtist("artist3", type2, group1);
		Artist group2 = createArtist("group2", type1, null);
		Artist artist4 = createArtist("artist4", type2, group2);
		em.persist(group1);
		em.persist(group2);
		em.persist(artist2);
		em.persist(artist3);
		em.persist(artist4);

		Artist paramGroup = Artist.builder()
			.id(group1.getId())
			.build();

		//when
		List<Artist> artists = artistRepository.findByGroup(paramGroup);

		//then
		assertThat(artists).hasSize(2)
			.extracting("id", "name", "group.id")
			.containsExactlyInAnyOrder(
				Tuple.tuple(artist2.getId(), artist2.getName(), group1.getId()),
				Tuple.tuple(artist3.getId(), artist3.getName(), group1.getId()));
	}

	@DisplayName("pk 목록으로 artist 목록 조회")
	@Test
	void findByIdIn() throws Exception {
		//given
		Artist artist1 = createArtist("artist1", null, null);
		Artist artist2 = createArtist("artist2", null, null);
		Artist artist3 = createArtist("artist3", null, null);
		Artist artist4 = createArtist("artist4", null, null);
		em.persist(artist1);
		em.persist(artist2);
		em.persist(artist3);
		em.persist(artist4);

		List<Long> inIds = List.of(artist2.getId(), artist4.getId());

		//when
		List<Artist> findArtists = artistRepository.findByIdIn(inIds);

		//then
		assertThat(findArtists).hasSize(2)
			.extracting("name")
			.containsExactlyInAnyOrder("artist2", "artist4");
	}

	@DisplayName("아티스트 구분으로 아티스트 수 조회")
	@Test
	void countByArtistType() throws Exception {
		//given
		ArtistType type = createArtistType("type");
		em.persist(type);

		Artist artist1 = createArtist("artist1", type, null);
		Artist artist2 = createArtist("artist2", type, null);
		Artist artist3 = createArtist("artist3", type, null);
		em.persist(artist1);
		em.persist(artist2);
		em.persist(artist3);

		//when
		Long artistCount = artistRepository.countByArtistType(type);

		//then
		assertThat(artistCount).isEqualTo(3L);
	}

	@DisplayName("Artist group을 null로 변경한다")
	@Test
	void bulkGroupToNull() throws Exception {
		//given
		Artist group = createArtist("group", null, null);
		Artist artist1 = createArtist("artist1", null, group);
		Artist artist2 = createArtist("artist2", null, group);
		em.persist(group);
		em.persist(artist1);
		em.persist(artist2);

		//when
		int bulkCount = artistRepository.bulkGroupToNull(group.getId());

		//then
		assertThat(bulkCount).isEqualTo(2);
		Artist findArtist1 = artistRepository.findById(artist1.getId()).get();
		assertThat(findArtist1).extracting("group").isNull();
	}

	private ArtistType createArtistType(String type) {
		return ArtistType.builder().type(type).build();
	}

	private Artist createArtist(String name, ArtistType type, Artist group) {
		return Artist.builder()
			.name(name)
			.artistType(type)
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
				.extracting("name", "artistType.id")
				.containsExactlyInAnyOrder(
					Tuple.tuple("artist2", type2.getId()),
					Tuple.tuple("artist3", type2.getId()));

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
				.extracting("name", "group.name", "artistType.id")
				.containsExactlyInAnyOrder(
					Tuple.tuple("artist2", "group1", type2.getId()),
					Tuple.tuple("artist3", "group1", type2.getId()));

			assertThat(findArtists.getTotalElements()).isEqualTo(2);
			assertThat(findArtists.getTotalPages()).isEqualTo(1);
		}

	}
}
