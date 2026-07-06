package com.sofka.msa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = cb.createQuery(domainType);
		Root<T> root = criteriaQuery.from(domainType);
		criteriaQuery.select(root);
		TypedQuery<T> query = em.createQuery(criteriaQuery);
		return query.getResultList();
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
