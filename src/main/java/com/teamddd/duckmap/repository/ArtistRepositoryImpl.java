package com.teamddd.duckmap.repository;

import static com.teamddd.duckmap.entity.QArtist.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teamddd.duckmap.entity.Artist;
import com.teamddd.duckmap.entity.QArtist;

public class ArtistRepositoryImpl implements ArtistRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private final QArtist group = new QArtist("group");

	public ArtistRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<Artist> findByTypeAndName(Long artistTypeId, String name, Pageable pageable) {
		List<Artist> artists = queryFactory.selectFrom(artist)
			.leftJoin(artist.group, group).fetchJoin()
			.where(eqArtistTypeId(artistTypeId),
				containsArtistName(name)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		for (Artist artist1 : artists) {
			System.out.println(artist1.getName());
		}

		JPAQuery<Long> countQuery = queryFactory.select(artist.count())
			.from(artist)
			.leftJoin(artist.group, group)
			.where(eqArtistTypeId(artistTypeId),
				containsArtistName(name)
			);

		return PageableExecutionUtils.getPage(artists, pageable, countQuery::fetchOne);
	}

	private BooleanExpression eqArtistTypeId(Long artistTypeId) {
		return artistTypeId != null ? artist.artistType.id.eq(artistTypeId) : null;
	}

	private BooleanExpression containsArtistName(String name) {
		return StringUtils.hasText(name) ? artist.name.contains(name).or(group.name.contains(name)) : null;
	}
}
