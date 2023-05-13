package com.teamddd.duckmap.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.dto.Result;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

	@Operation(summary = "이미지 저장")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result<ImageRes> saveImage(@RequestParam MultipartFile file) {
		return Result.<ImageRes>builder()
			.data(
				ImageRes.builder()
					.apiUrl("/images/")
					.filename("filename.jpg")
					.build()
			)
			.build();
	}
}
