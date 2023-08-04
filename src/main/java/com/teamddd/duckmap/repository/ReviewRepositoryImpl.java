package com.teamddd.duckmap.repository;

import static com.teamddd.duckmap.entity.QEvent.*;
import static com.teamddd.duckmap.entity.QEventArtist.*;
import static com.teamddd.duckmap.entity.QReview.*;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teamddd.duckmap.dto.review.QReviewEventDto;
import com.teamddd.duckmap.dto.review.ReviewEventDto;
import com.teamddd.duckmap.entity.Review;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public ReviewRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<Review> findByArtistAndDate(Long artistId, LocalDate date, Pageable pageable) {
		List<Review> reviews = queryFactory.selectDistinct(review)
			.from(review)
			.leftJoin(review.event, event).fetchJoin()
			.join(eventArtist).on(event.eq(eventArtist.event))
			.where(eqArtistId(artistId), betweenDate(date))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory.select(review.count())
			.from(review)
			.leftJoin(review.event, event)
			.join(eventArtist).on(event.eq(eventArtist.event))
			.where(eqArtistId(artistId), betweenDate(date));

		return PageableExecutionUtils.getPage(reviews, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<ReviewEventDto> findWithEventByMemberId(Long memberId, Pageable pageable) {
		List<ReviewEventDto> reviews = queryFactory.select(
				new QReviewEventDto(
					review,
					event.id,
					event.storeName
				))
			.from(review)
			.leftJoin(review.event, event).fetchJoin()
			.where(review.member.id.eq(memberId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory.select(review.count())
			.from(review)
			.leftJoin(review.event, event)
			.where(review.member.id.eq(memberId));

		return PageableExecutionUtils.getPage(reviews, pageable, countQuery::fetchOne);
	}

	private BooleanExpression eqArtistId(Long artistId) {
		return artistId != null ? eventArtist.artist.id.eq(artistId) : null;
	}

	private BooleanExpression betweenDate(LocalDate date) {
		return date != null ? event.fromDate.loe(date).and(event.toDate.goe(date)) : null;
	}
}
