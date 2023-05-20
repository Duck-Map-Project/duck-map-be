package com.teamddd.duckmap.dto.review;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class UpdateReviewReq {
	@Min(0)
	@Max(5)
	private int score;
	private String content;
	@NotNull
	@Size(min = 1)
	private List<String> imageFilenames;
}
