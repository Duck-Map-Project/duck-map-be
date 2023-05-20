package com.teamddd.duckmap.dto.review;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import lombok.Getter;

@Getter
public class UpdateReviewReq {
	@Range(min = 0, max = 5)
	private int score;
	private String content;
	@NotNull
	@Size(min = 1)
	private List<String> imageFilenames;
}
