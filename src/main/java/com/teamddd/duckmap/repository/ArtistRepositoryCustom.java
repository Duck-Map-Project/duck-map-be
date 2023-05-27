package com.teamddd.duckmap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.teamddd.duckmap.entity.Artist;

public interface ArtistRepositoryCustom {

	Page<Artist> findByTypeAndName(Long artistTypeId, String name, Pageable pageable);
}
