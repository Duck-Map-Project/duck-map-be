package com.teamddd.duckmap.dto.event.bookmark;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class CreateBookmarkReq {
	@NotNull
	private Long bookmarkFolderId;
}
