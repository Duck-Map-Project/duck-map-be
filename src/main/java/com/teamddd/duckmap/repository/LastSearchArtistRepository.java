package com.teamddd.duckmap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamddd.duckmap.entity.LastSearchArtist;

public interface LastSearchArtistRepository extends JpaRepository<LastSearchArtist, Long> {
	Optional<LastSearchArtist> findByUserId(Long userId);
}
