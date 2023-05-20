package com.teamddd.duckmap.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class CreateBookmarkReq {
	private Long bookmarkFolderId;
	@NotNull
	private Long eventId;
}
