package com.teamddd.duckmap.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Result<T> {
	private T data;
}
