package com.teamddd.duckmap.dto.event.event;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.teamddd.duckmap.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventSearchServiceReq {
	private Optional<Member> member;
	private LocalDate date;
	private Long artistId;
	private boolean onlyInProgress;
	private Pageable pageable;
}
