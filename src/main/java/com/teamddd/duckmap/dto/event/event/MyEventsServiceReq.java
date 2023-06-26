package com.teamddd.duckmap.dto.event.event;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MyEventsServiceReq {
	private Long memberId;
	private LocalDate date;
	private Pageable pageable;
}
