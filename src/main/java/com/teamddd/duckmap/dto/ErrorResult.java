package com.teamddd.duckmap.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResult {
	private String code;
	private String message;
}
