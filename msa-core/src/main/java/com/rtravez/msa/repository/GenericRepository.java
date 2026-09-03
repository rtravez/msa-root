package com.rtravez.msa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.dsl.EntityPathBase;
import org.springframework.data.repository.NoRepositoryBean;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public abstract class GenericRepository<T, ID extends Serializable> implements IGenericRepository<T, ID> {

	protected EntityManager em;
	protected JPAQueryFactory queryFactory;
	protected Class<T> domainType;

	public EntityManager getEntityManager() {
		return em;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	protected GenericRepository(Class<T> domainType) {
		this.domainType = domainType;
	}

	@Override
	public T save(T t) {
		em.persist(t);
		return t;
	}

	@Override
	public void delete(T t) {
		em.remove(em.merge(t));
	}

	@Override
	public T update(T t) {
		return em.merge(t);
	}

	@Override
	public List<T> findAll() {
		EntityPathBase<T> entityPath = new EntityPathBase<>(domainType, domainType.getSimpleName());
		return queryFactory.selectFrom(entityPath).fetch();
	}

	@Override
	public Optional<T> findById(ID id) {
		return Optional.ofNullable(em.find(domainType, id));
	}

	@Override
	public void deleteById(ID id) {
		this.findById(id).ifPresent(this::delete);
	}
}
