package com.teamddd.duckmap.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@ConfigurationProperties(prefix = "props")
@ConstructorBinding
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Props {
	private String imageDir;
}
