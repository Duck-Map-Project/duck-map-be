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

class UpdateArtistTypeReqTest {
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

	@DisplayName("type은 필수값이다")
	@Test
	void notBlankType() throws Exception {
		//given
		UpdateArtistTypeReq req = new UpdateArtistTypeReq();
		ReflectionTestUtils.setField(req, "type", " ");

		//when
		Set<ConstraintViolation<UpdateArtistTypeReq>> validate = validator.validateProperty(req, "type");

		//then
		assertThat(validate).hasSize(1)
			.extracting(ConstraintViolation::getMessage)
			.containsOnly("공백일 수 없습니다");
	}
}
