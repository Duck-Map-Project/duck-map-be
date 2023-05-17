package com.teamddd.duckmap.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CreateReviewReq {
	@NotNull
	private Long eventId;
	@Range(min = 0, max = 5)
	@Schema(defaultValue = "0")
	private int score;
	private String content;
	@NotNull
	@Size(min = 1)
	private List<String> imageFilenames;
}
