package com.rtravez.msa.repository;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface IGenericRepository<T, K> {

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
    void deleteById(K id);

    /**
     * Find by id
     *
     * @param id
     * @return
     */
    Optional<T> findById(K id);

    /**
     * Find all
     *
     * @return
     */
    List<T> findAll();
}
