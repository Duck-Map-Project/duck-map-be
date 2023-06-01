package com.teamddd.duckmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamddd.duckmap.entity.LastSearchArtist;

public interface LastSearchArtistRepository extends JpaRepository<LastSearchArtist, Long> {
	LastSearchArtist findByUserId(Long userId);
}
