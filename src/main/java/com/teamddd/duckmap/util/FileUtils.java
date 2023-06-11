package com.teamddd.duckmap.util;

import java.io.File;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import com.teamddd.duckmap.common.ExceptionCodeMessage;
import com.teamddd.duckmap.exception.NonExistentFileException;
import com.teamddd.duckmap.exception.ServiceException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

	public static String getFullPath(String dir, String fileName) {
		return dir + fileName;
	}

	public static Resource getResource(String dir, String filename) {
		Resource resource;
		String url = getFullPath(dir, filename);

		File file = new File(url);
		if (!file.exists()) {
			log.error(ExceptionCodeMessage.NON_EXISTENT_FILE_EXCEPTION.message() + ": {}", url);
			throw new NonExistentFileException(url);
		}

		try {
			resource = new UrlResource("file:" + url);
		} catch (MalformedURLException e) {
			log.error("유효하지 않은 URL입니다: {}", url, e);
			throw new ServiceException("유효하지 않은 URL입니다: " + url, e);
		}
		return resource;
	}
}
