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
import com.teamddd.duckmap.entity.Review;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public ReviewRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<Review> findByArtistAndDate(Long artistId, LocalDate date, Pageable pageable) {
		List<Review> reviews = queryFactory.select(review)
			.from(review)
			.leftJoin(review.event, event)
			.join(eventArtist).on(review.event.eq(eventArtist.event))
			.where(eqArtistId(artistId), betweenDate(date))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory.select(review.count())
			.from(review)
			.leftJoin(review.event, event)
			.join(eventArtist).on(review.event.eq(eventArtist.event))
			.where(eqArtistId(artistId), betweenDate(date));

		return PageableExecutionUtils.getPage(reviews, pageable, countQuery::fetchOne);
	}

	private BooleanExpression eqArtistId(Long artistId) {
		return artistId != null ? eventArtist.artist.id.eq(artistId) : null;
	}

	private BooleanExpression betweenDate(LocalDate date) {
		return date != null ? event.fromDate.loe(date).and(event.toDate.goe(date)) : null;
	}
}
