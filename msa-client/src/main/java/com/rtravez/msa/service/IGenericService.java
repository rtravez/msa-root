package com.rtravez.msa.service;

import com.rtravez.msa.exception.ExceptionManager;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IGenericService<T, ID extends Serializable> {

	T save(T entity) throws ExceptionManager;

	T update(T entity) throws ExceptionManager;

	Optional<T> findById(ID id) throws ExceptionManager;

	List<T> findAll() throws ExceptionManager;

	void deleteById(ID id) throws ExceptionManager;

	void delete(T entity) throws ExceptionManager;
}
