package com.rtravez.msa.service;

import com.rtravez.msa.exception.ExceptionManager;

import java.util.List;
import java.util.Optional;

public interface IGenericService<T, K> {

	T save(T entity) throws ExceptionManager;

	T update(T entity) throws ExceptionManager;

	Optional<T> findById(K id) throws ExceptionManager;

	List<T> findAll() throws ExceptionManager;

	void deleteById(K id) throws ExceptionManager;

	void delete(T entity) throws ExceptionManager;
}
