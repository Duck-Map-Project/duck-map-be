package com.teamddd.duckmap.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.dto.artist.ArtistRes;
import com.teamddd.duckmap.dto.artist.ArtistSearchParam;
import com.teamddd.duckmap.dto.artist.CreateArtistReq;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.ArtistType;
import com.teamddd.duckmap.repository.ArtistRepository;

@Transactional
@SpringBootTest
class ArtistServiceTest {

	@SpyBean
	ArtistService artistService;
	@SpyBean
	ArtistRepository artistRepository;
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
		assertThat(findArtist.get()).extracting("id", "name")
			.contains(1L, "artist1");
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
			artistRepository.findByTypeAndName(param.getArtistTypeId(), param.getArtistName(), pageRequest))
			.thenReturn(new PageImpl<>(artists, pageRequest, artists.size()));

		//when
		Page<ArtistRes> artistResPage = artistService.getArtistResPageByTypeAndName(param, pageRequest);

		//then
		assertThat(artistResPage).hasSize(7)
			.extracting("artistType.type", "name", "groupName")
			.containsExactlyInAnyOrder(
				Tuple.tuple("type1", "group1", null),
				Tuple.tuple("type1", "group2", null),
				Tuple.tuple("type2", "artist3", "group1"),
				Tuple.tuple("type2", "artist4", "group1"),
				Tuple.tuple("type2", "artist5", "group2"),
				Tuple.tuple("type2", "artist6", "group2"),
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
		Artist group = Artist.builder()
			.id(groupId)
			.build();

		when(artistRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(artistRepository.findByGroup(group))
			.thenReturn(artists);

		//when
		List<ArtistRes> artistResList = artistService.getArtistsByGroup(groupId);

		//then
		assertThat(artistResList).hasSize(3)
			.extracting("artistType.type", "name", "groupName")
			.containsExactlyInAnyOrder(
				Tuple.tuple("type2", "artist2", "group1"),
				Tuple.tuple("type2", "artist3", "group1"),
				Tuple.tuple("type2", "artist4", "group1"));
	}

	ArtistType createArtistType(String type) {
		return ArtistType.builder()
			.type(type)
			.build();
	}

	Artist createArtist(ArtistType type, String name, Artist group) {
		return Artist.builder()
			.artistType(type)
			.name(name)
			.group(group)
			.build();
	}
}
