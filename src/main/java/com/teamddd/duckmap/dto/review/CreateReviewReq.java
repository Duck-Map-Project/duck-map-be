package com.teamddd.duckmap.dto.review;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CreateReviewReq {
	@NotNull
	private Long eventId;
	@Min(0)
	@Max(5)
	@Schema(defaultValue = "0")
	private int score;
	private String content;
	@NotNull
	@Size(min = 1)
	private List<String> imageFilenames;
}
