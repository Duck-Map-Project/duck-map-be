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

class UpdateArtistReqTest {

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

	@DisplayName("artistTypeId는 필수값이다")
	@Test
	void notNullArtistTypeId() throws Exception {
		//given
		UpdateArtistReq req = new UpdateArtistReq();

		//when
		Set<ConstraintViolation<UpdateArtistReq>> validate = validator.validateProperty(req, "artistTypeId");

		//then
		assertThat(validate).hasSize(1)
			.extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessageTemplate)
			.containsExactlyInAnyOrder(Tuple.tuple("artistTypeId", "{javax.validation.constraints.NotNull.message}"));
	}

	@DisplayName("name은 필수값이다")
	@Test
	void notBlankName() throws Exception {
		//given
		UpdateArtistReq req = new UpdateArtistReq();
		ReflectionTestUtils.setField(req, "name", " ");

		//when
		Set<ConstraintViolation<UpdateArtistReq>> validate = validator.validateProperty(req, "name");

		//then
		assertThat(validate).hasSize(1)
			.extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessageTemplate)
			.containsExactlyInAnyOrder(Tuple.tuple("name", "{javax.validation.constraints.NotBlank.message}"));
	}

	@DisplayName("image는 필수값이다")
	@Test
	void notBlankImage() throws Exception {
		//given
		UpdateArtistReq req = new UpdateArtistReq();
		ReflectionTestUtils.setField(req, "image", " ");

		//when
		Set<ConstraintViolation<UpdateArtistReq>> validate = validator.validateProperty(req, "image");

		//then
		assertThat(validate).hasSize(1)
			.extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessageTemplate)
			.containsExactlyInAnyOrder(Tuple.tuple("image", "{javax.validation.constraints.NotBlank.message}"));
	}

}
