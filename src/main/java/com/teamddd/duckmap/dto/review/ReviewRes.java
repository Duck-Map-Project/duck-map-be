package com.teamddd.duckmap.dto.review;

import java.time.LocalDateTime;
import java.util.List;

import com.teamddd.duckmap.dto.ImageRes;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewRes {
	private Long id;
	private ImageRes userProfile;
	private String username;
	private LocalDateTime createdAt;
	private int score;
	private String content;
	private List<ImageRes> photos;
}
