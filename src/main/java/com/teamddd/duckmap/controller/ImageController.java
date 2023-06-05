package com.teamddd.duckmap.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.teamddd.duckmap.dto.ImageRes;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

	@Operation(summary = "이미지 저장", description = "content type은 image로 시작해야 합니다")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ImageRes saveImage(@RequestParam MultipartFile file) {
		if (file.isEmpty() || file.getContentType() == null || !file.getContentType().startsWith("image")) {
			return ImageRes.builder()
				.filename("구현하면 400 반환 예정 - content type은 image로 시작해야 합니다")
				.build();
		}
		return ImageRes.builder()
			.filename("filename.jpg")
			.build();
	}

	@Operation(summary = "이미지 조회")
	@GetMapping("/{filename}")
	public Resource loadImage(@PathVariable String filename) {
		return new ClassPathResource("static/mock/img.png");
	}
}
