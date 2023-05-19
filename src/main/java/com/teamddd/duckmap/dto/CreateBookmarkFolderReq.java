package com.teamddd.duckmap.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class CreateBookmarkFolderReq {
	@NotBlank
	private String name;
	private String image;
	private List<Long> bookmarkIds;
}
