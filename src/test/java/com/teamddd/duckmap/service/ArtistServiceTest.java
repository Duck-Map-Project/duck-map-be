package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.common.Props;
import com.teamddd.duckmap.dto.artist.ArtistRes;
import com.teamddd.duckmap.dto.artist.ArtistSearchParam;
import com.teamddd.duckmap.dto.artist.CreateArtistReq;
import com.teamddd.duckmap.dto.artist.UpdateArtistServiceReq;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.ArtistType;
import com.teamddd.duckmap.exception.NonExistentArtistException;
import com.teamddd.duckmap.repository.ArtistRepository;
import com.teamddd.duckmap.repository.EventArtistRepository;
import com.teamddd.duckmap.repository.LastSearchArtistRepository;

@Transactional
@SpringBootTest
class ArtistServiceTest {

	@SpyBean
	ArtistService artistService;
	@SpyBean
	ArtistRepository artistRepository;
	@MockBean
	ArtistTypeService artistTypeService;
	@MockBean
	LastSearchArtistRepository lastSearchArtistRepository;
	@MockBean
	EventArtistRepository eventArtistRepository;
	@Autowired
	Props props;
	@Autowired
	EntityManager em;

	@DisplayName("아티스트를 등록한다")
	@Test
	void createArtist() throws Exception {
		//given
		ArtistType artistType = createArtistType("artistType");
		em.persist(artistType);

		CreateArtistReq request = new CreateArtistReq();
		ReflectionTestUtils.setField(request, "name", "artist1");
		ReflectionTestUtils.setField(request, "image", "filename.png");
		ReflectionTestUtils.setField(request, "artistTypeId", artistType.getId());

		//when
		Long artistId = artistService.createArtist(request);

		//then
		assertThat(artistId).isNotNull();

		Optional<Artist> findArtist = artistRepository.findById(artistId);
		assertThat(findArtist).isNotEmpty();
		assertThat(findArtist.get()).extracting("name").isEqualTo("artist1");
	}

	@DisplayName("조회한 Page<Artist>를 Page<ArtistRes>로 변환하여 반환한다")
	@Test
	void getArtistResPageByTypeAndName() throws Exception {
		//given
		ArtistType type1 = createArtistType("type1");
		ArtistType type2 = createArtistType("type2");

		Artist artist1 = createArtist(type1, "group1", null);
		Artist artist2 = createArtist(type1, "group2", null);
		Artist artist3 = createArtist(type2, "artist3", artist1);
		Artist artist4 = createArtist(type2, "artist4", artist1);
		Artist artist5 = createArtist(type2, "artist5", artist2);
		Artist artist6 = createArtist(type2, "artist6", artist2);
		Artist artist7 = createArtist(type2, "artist7", artist2);
		List<Artist> artists = List.of(artist1, artist2, artist3, artist4, artist5, artist6, artist7);

		ArtistSearchParam param = ArtistSearchParam.builder().build();
		PageRequest pageRequest = PageRequest.of(0, artists.size());

		when(
			artistRepository.findByTypeAndName(param.getArtistTypeId(), param.getArtistName(), pageRequest)).thenReturn(
			new PageImpl<>(artists, pageRequest, artists.size()));

		//when
		Page<ArtistRes> artistResPage = artistService.getArtistResPageByTypeAndName(param, pageRequest);

		//then
		assertThat(artistResPage).hasSize(7)
			.extracting("artistType.type", "name", "groupName")
			.containsExactlyInAnyOrder(Tuple.tuple("type1", "group1", null), Tuple.tuple("type1", "group2", null),
				Tuple.tuple("type2", "artist3", "group1"), Tuple.tuple("type2", "artist4", "group1"),
				Tuple.tuple("type2", "artist5", "group2"), Tuple.tuple("type2", "artist6", "group2"),
				Tuple.tuple("type2", "artist7", "group2"));
	}

	@DisplayName("소속 아티스트 목록을 조회한다")
	@Test
	void getArtistsByGroup() throws Exception {
		//given
		ArtistType type1 = createArtistType("type1");
		ArtistType type2 = createArtistType("type2");

		Artist artist1 = createArtist(type1, "group1", null);
		Artist artist2 = createArtist(type2, "artist2", artist1);
		Artist artist3 = createArtist(type2, "artist3", artist1);
		Artist artist4 = createArtist(type2, "artist4", artist1);
		List<Artist> artists = List.of(artist2, artist3, artist4);

		Long groupId = 1L;
		Artist group = Artist.builder().id(groupId).build();

		when(artistRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(artistRepository.findByGroup(group)).thenReturn(artists);

		//when
		List<ArtistRes> artistResList = artistService.getArtistsByGroup(groupId);

		//then
		assertThat(artistResList).hasSize(3)
			.extracting("artistType.type", "name", "groupName")
			.containsExactlyInAnyOrder(Tuple.tuple("type2", "artist2", "group1"),
				Tuple.tuple("type2", "artist3", "group1"), Tuple.tuple("type2", "artist4", "group1"));
	}

	@DisplayName("아티스트를 삭제한다")
	@Test
	void deleteArtist() throws Exception {
		//given
		Artist artist1 = createArtist(null, "artist1", null);
		em.persist(artist1);

		when(artistRepository.updateGroupToNull(anyLong())).thenReturn(0);
		when(eventArtistRepository.updateArtistToNull(anyLong())).thenReturn(0);
		when(lastSearchArtistRepository.deleteByArtistId(anyLong())).thenReturn(0);

		Long deleteArtistId = artist1.getId();
		//when
		artistService.deleteArtist(deleteArtistId);
		em.flush();
		em.clear();

		//then
		Optional<Artist> findArtist = artistRepository.findById(deleteArtistId);
		assertThat(findArtist).isEmpty();
	}

	ArtistType createArtistType(String type) {
		return ArtistType.builder().type(type).build();
	}

	Artist createArtist(ArtistType type, String name, Artist group) {
		return Artist.builder().artistType(type).name(name).group(group).image("image").build();
	}

	@DisplayName("아티스트 id 목록으로 아티스트 목록을 조회한다")
	@Nested
	class GetArtistsByIds {
		@DisplayName("유효한 id로만 조회한 경우")
		@Test
		void getArtistsByIds1() throws Exception {
			//given
			Artist artist1 = createArtist(null, "artist1", null);
			Artist artist2 = createArtist(null, "artist2", null);
			Artist artist3 = createArtist(null, "artist3", null);
			artistRepository.saveAll(List.of(artist1, artist2, artist3));

			List<Long> inIds = List.of(artist2.getId(), artist3.getId());

			//when
			List<Artist> findArtists = artistService.getArtistsByIds(inIds);

			//then
			assertThat(findArtists).hasSize(2).extracting("name").containsExactlyInAnyOrder("artist2", "artist3");
		}

		@DisplayName("유효하지 않은 id가 포함된 조회한 경우")
		@Test
		void getArtistsByIds2() throws Exception {
			//given
			Artist artist1 = createArtist(null, "artist1", null);
			Artist artist2 = createArtist(null, "artist2", null);
			Artist artist3 = createArtist(null, "artist3", null);
			artistRepository.saveAll(List.of(artist1, artist2, artist3));

			List<Long> inIds = List.of(artist2.getId(), 100L);

			//when //then
			assertThatThrownBy(() -> artistService.getArtistsByIds(inIds)).isInstanceOf(
				NonExistentArtistException.class);
		}
	}

	@DisplayName("아티스트 정보를 변경한다")
	@Nested
	class UpdateArtist {
		@DisplayName("groupId가 null인 경우 & 이미지 변경")
		@Test
		void updateArtist1() throws Exception {
			//given
			ArtistType type = createArtistType("type");
			em.persist(type);

			Artist group = createArtist(null, "group", null);
			Artist artist = createArtist(null, "artist", group);
			em.persist(group);
			em.persist(artist);

			em.flush();
			em.clear();

			when(artistRepository.findById(group.getId())).thenReturn(Optional.of(group));
			when(artistTypeService.getArtistType(any())).thenReturn(type);

			Long updateArtistId = artist.getId();
			UpdateArtistServiceReq request = UpdateArtistServiceReq.builder()
				.id(updateArtistId)
				.groupId(null)
				.name("new_name")
				.image("new_image")
				.artistTypeId(type.getId())
				.build();
			//when
			artistService.updateArtist(request);
			em.flush();
			em.clear();

			//then
			Artist findArtist = artistRepository.findById(updateArtistId).get();
			assertThat(findArtist).extracting("group", "name", "image", "artistType.type")
				.containsOnly(null, "new_name", "new_image", "type");
		}

		@DisplayName("groupId가 null이 아닌 경우")
		@Test
		void updateArtist2() throws Exception {
			//given
			ArtistType type = createArtistType("type");
			em.persist(type);

			Artist group = createArtist(null, "group", null);
			Artist artist = createArtist(null, "artist", null);
			em.persist(group);
			em.persist(artist);

			em.flush();
			em.clear();

			when(artistRepository.findById(group.getId())).thenReturn(Optional.of(group));
			when(artistTypeService.getArtistType(any())).thenReturn(type);

			Long updateArtistId = artist.getId();
			UpdateArtistServiceReq request = UpdateArtistServiceReq.builder()
				.id(updateArtistId)
				.groupId(group.getId())
				.name("new_name")
				.image("image")
				.artistTypeId(type.getId())
				.build();
			//when
			artistService.updateArtist(request);
			em.flush();
			em.clear();

			//then
			Artist findArtist = artistRepository.findById(updateArtistId).get();
			assertThat(findArtist).extracting("group.name", "name", "image", "artistType.type")
				.containsOnly("group", "new_name", "image", "type");
		}
	}
}
