package com.teamddd.duckmap.dto.event.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EventPageReq {
	@Schema(defaultValue = "0")
	private Integer page;

	@Schema(defaultValue = "20")
	private Integer size;

	@Schema(defaultValue = "DESC")
	private Sort.Direction direction;

	@Schema(defaultValue = "likeCount", description = "likeCount: 좋아요수, reviewCount: 리뷰수")
	private String sortProperty;

	@Builder
	public EventPageReq(Integer page, Integer size, Sort.Direction direction, String sortProperty) {
		this.page = page == null ? 0 : page;
		this.size = size == null ? 20 : size;
		this.direction = direction == null ? Sort.Direction.DESC : direction;
		this.sortProperty = sortProperty == null ? "likeCount" : sortProperty;
	}

	public Pageable toPageable() {
		return PageRequest.of(page, size, Sort.by(direction, sortProperty));
	}
}
