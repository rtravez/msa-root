package com.rtravez.msa.repository;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public abstract class BaseRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID>
		implements BaseRepository<T, ID> {
	protected final JPAQueryFactory queryFactory;

	protected BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		this.queryFactory = new JPAQueryFactory(entityManager);
	}
}