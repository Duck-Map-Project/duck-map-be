package com.teamddd.duckmap.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teamddd.duckmap.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {

	@Query("select e.hashtag from Event e where e.fromDate <= :date and e.toDate >= :date")
	List<String> findHashtagsByFromDateAndToDate(@Param("date") LocalDate date);
}
