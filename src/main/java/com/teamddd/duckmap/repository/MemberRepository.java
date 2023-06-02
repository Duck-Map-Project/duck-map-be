package com.teamddd.duckmap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamddd.duckmap.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	Optional<Member> findByUsername(String username);
}
