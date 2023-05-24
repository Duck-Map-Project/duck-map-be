package com.teamddd.duckmap.dto.event.bookmark;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class CreateBookmarkFolderReq {
	@NotBlank
	private String name;
	private String image;
}
