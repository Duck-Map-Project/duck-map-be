package com.teamddd.duckmap.dto.review;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewSearchServiceReq {
	private LocalDate date;
	private Long artistId;
	private boolean onlyInProgress;
	private Pageable pageable;
}
