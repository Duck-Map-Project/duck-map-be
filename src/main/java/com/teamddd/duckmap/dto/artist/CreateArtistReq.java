package com.teamddd.duckmap.dto.artist;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class CreateArtistReq {
	@NotNull(message = "아티스트 구분은 필수값입니다")
	private Long artistTypeId;
	private Long groupId;
	@NotBlank(message = "아티스트 이름은 필수값입니다")
	private String name;
	@NotBlank(message = "아티스트 사진은 필수값입니다")
	private String image;
}
