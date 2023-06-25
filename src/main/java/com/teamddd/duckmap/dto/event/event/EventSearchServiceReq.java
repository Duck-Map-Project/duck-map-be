package com.teamddd.duckmap.dto.event.event;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventSearchServiceReq {
	private Long memberId;
	private LocalDate date;
	private Long artistId;
	private boolean onlyInProgress;
	private Pageable pageable;
}
