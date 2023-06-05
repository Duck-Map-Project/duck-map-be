package com.teamddd.duckmap.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ImageRes {
	private String fileUrl;

	@Builder
	public ImageRes(String filename) {
		this.fileUrl = "/images/" + filename;
	}
}
