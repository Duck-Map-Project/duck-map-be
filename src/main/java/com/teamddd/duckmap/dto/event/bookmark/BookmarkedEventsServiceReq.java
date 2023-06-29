package com.teamddd.duckmap.dto.event.bookmark;

import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookmarkedEventsServiceReq {
	private Long bookmarkFolderId;
	private Pageable pageable;
}
