package com.teamddd.duckmap.dto.artist;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class CreateArtistTypeReqTest {

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
		CreateArtistTypeReq req = new CreateArtistTypeReq();
		ReflectionTestUtils.setField(req, "type", " ");

		//when
		Set<ConstraintViolation<CreateArtistTypeReq>> validate = validator.validateProperty(req, "type");

		//then
		assertThat(validate).hasSize(1)
			.extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessageTemplate)
			.containsExactlyInAnyOrder(Tuple.tuple("type", "{javax.validation.constraints.NotBlank.message}"));
	}
}
