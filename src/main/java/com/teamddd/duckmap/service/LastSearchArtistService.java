package com.teamddd.duckmap.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.LastSearchArtist;
import com.teamddd.duckmap.entity.Member;
import com.teamddd.duckmap.repository.LastSearchArtistRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LastSearchArtistService {
	private final LastSearchArtistRepository lastSearchArtistRepository;

	@Transactional
	public void saveLastSearchArtist(Member member, Artist artist) {
		Optional<LastSearchArtist> lastSearch = lastSearchArtistRepository.findByMember(member);

		lastSearch.ifPresentOrElse(
			lastSearchArtist -> lastSearchArtist.changeSearchArtist(artist),
			() -> lastSearchArtistRepository.save(
				LastSearchArtist.builder()
					.member(member)
					.artist(artist)
					.build()
			)
		);
	}
}
