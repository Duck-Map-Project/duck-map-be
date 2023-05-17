package com.teamddd.duckmap.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageRes {
	private String apiUrl;
	private String filename;
}
