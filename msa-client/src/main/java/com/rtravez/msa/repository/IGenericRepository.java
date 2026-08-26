package com.rtravez.msa.repository;

import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface IGenericRepository<T, ID extends Serializable> {

    /**
     * Save
     *
     * @param t
     * @return
     */
    T save(T t);

    /**
     * Update
     *
     * @param t
     * @return
     */
    T update(T t);

    /**
     * Delete
     *
     * @param t
     */
    void delete(T t);

    /**
     * Delete by id
     *
     * @param id
     */
    void deleteById(ID id);

    /**
     * Find by id
     *
     * @param id
     * @return
     */
    Optional<T> findById(ID id);

    /**
     * Find all
     *
     * @return
     */
    List<T> findAll();
}
