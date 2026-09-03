package com.rtravez.msa.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.rtravez.msa.exception.ExceptionManager;
import com.rtravez.msa.repository.IGenericRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class GenericService<T, ID extends Serializable, R extends IGenericRepository<T, ID>> implements IGenericService<T, ID> {

	protected final R repository;

	protected GenericService(R repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public T save(T entity) throws ExceptionManager {
		try {
			return repository.save(entity);
		} catch (Exception e) {
			log.error("save: ", e);
			throw new ExceptionManager.GettingException("Error al guardar el registro");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<T> findById(ID id) throws ExceptionManager {
		try {
			return repository.findById(id);
		} catch (Exception e) {
			log.error("findById: ", e);
			throw new ExceptionManager.FindingException("Error al buscar el registro");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> findAll() throws ExceptionManager {
		try {
			return repository.findAll();
		} catch (Exception e) {
			log.error("findAll: ", e);
			throw new ExceptionManager.FindingException("Error al buscar los registros");
		}
	}

	@Override
	@Transactional
	public void deleteById(ID id) throws ExceptionManager {
		try {
			repository.deleteById(id);
		} catch (Exception e) {
			log.error("deleteById: ", e);
			throw new ExceptionManager.GettingException("Error al eliminar el registro");
		}
	}

	@Override
	@Transactional
	public void delete(T entity) throws ExceptionManager {
		try {
			repository.delete(entity);
		} catch (Exception e) {
			log.error("delete: ", e);
			throw new ExceptionManager.DeletingException("Error al eliminar el registro");
		}
	}

	@Override
	@Transactional
	public T update(T entity) throws ExceptionManager {
		try {
			return repository.update(entity);
		} catch (Exception e) {
			log.error("update: ", e);
			throw new ExceptionManager.GettingException("Error al actualizar el registro");
		}
	}
}
