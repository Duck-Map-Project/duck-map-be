package com.teamddd.duckmap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.ArtistType;

public interface ArtistRepository extends JpaRepository<Artist, Long>, ArtistRepositoryCustom {

	List<Artist> findByGroup(Artist group);

	List<Artist> findByIdIn(List<Long> id);

	Long countByArtistType(ArtistType artistType);
}
