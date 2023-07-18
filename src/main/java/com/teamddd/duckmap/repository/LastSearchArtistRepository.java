package com.teamddd.duckmap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teamddd.duckmap.entity.LastSearchArtist;
import com.teamddd.duckmap.entity.Member;

public interface LastSearchArtistRepository extends JpaRepository<LastSearchArtist, Long> {
	Optional<LastSearchArtist> findByMemberId(Long memberId);

	@Modifying(clearAutomatically = true)
	@Query("delete from LastSearchArtist lsa where lsa.artist.id = :artistId")
	int deleteByArtistId(@Param("artistId") Long artistId);

	Optional<LastSearchArtist> findByMember(Member member);

	@EntityGraph(attributePaths = {"artist"})
	Optional<LastSearchArtist> findWithArtistByMember(Member member);
}
