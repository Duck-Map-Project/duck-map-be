package com.teamddd.duckmap.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.teamddd.duckmap.common.Props;
import com.teamddd.duckmap.dto.ImageRes;
import com.teamddd.duckmap.exception.NotContentTypeImageException;
import com.teamddd.duckmap.util.FileUtils;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

	private final Props props;

	@Operation(summary = "이미지 저장", description = "content type은 image로 시작해야 합니다")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ImageRes saveImage(@RequestParam MultipartFile file) {
		if (file.isEmpty() || file.getContentType() == null || !file.getContentType().startsWith("image")) {
			throw new NotContentTypeImageException();
		}

		String imageDir = props.getImageDir();
		String filename = FileUtils.storeFile(file, imageDir);

		return ImageRes.builder()
			.filename(filename)
			.build();
	}

	@Operation(summary = "이미지 조회")
	@GetMapping("/{filename}")
	public Resource loadImage(@PathVariable String filename) {
		String imageDir = props.getImageDir();
		return FileUtils.getResource(imageDir, filename);
	}
}
