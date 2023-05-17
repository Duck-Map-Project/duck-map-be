package com.teamddd.duckmap.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewRes {
	private Long id;
	private ImageRes userProfile;
	private String username;
	private int score;
	private String content;
	private List<ImageRes> photos;
}
