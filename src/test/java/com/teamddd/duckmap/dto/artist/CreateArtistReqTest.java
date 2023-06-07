package com.teamddd.duckmap.dto.artist;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class CreateArtistReqTest {

	static ValidatorFactory validatorFactory;
	static Validator validator;

	@BeforeAll
	static void initialize() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@AfterAll
	static void destroy() {
		validatorFactory.close();
	}

	@DisplayName("아티스트 구분은 필수값이다")
	@Test
	void notNullArtistTypeId() throws Exception {
		//given
		CreateArtistReq req = new CreateArtistReq();

		//when
		Set<ConstraintViolation<CreateArtistReq>> validate = validator.validateProperty(req, "artistTypeId");

		//then
		assertThat(validate).hasSize(1)
			.extracting(ConstraintViolation::getMessage)
			.containsOnly("아티스트 구분은 필수값입니다");
	}

	@DisplayName("name은 필수값이다")
	@Test
	void notBlankName() throws Exception {
		//given
		CreateArtistReq req = new CreateArtistReq();
		ReflectionTestUtils.setField(req, "name", " ");

		//when
		Set<ConstraintViolation<CreateArtistReq>> validate = validator.validateProperty(req, "name");

		//then
		assertThat(validate).hasSize(1)
			.extracting(ConstraintViolation::getMessage)
			.containsOnly("아티스트 이름은 필수값입니다");
	}

	@DisplayName("image는 필수값이다")
	@Test
	void notBlankImage() throws Exception {
		//given
		CreateArtistReq req = new CreateArtistReq();
		ReflectionTestUtils.setField(req, "image", " ");

		//when
		Set<ConstraintViolation<CreateArtistReq>> validate = validator.validateProperty(req, "image");

		//then
		assertThat(validate).hasSize(1)
			.extracting(ConstraintViolation::getMessage)
			.containsOnly("아티스트 사진은 필수값입니다");
	}
}
