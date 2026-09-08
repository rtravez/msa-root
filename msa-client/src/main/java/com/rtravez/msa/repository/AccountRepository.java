package com.rtravez.msa.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rtravez.msa.entity.AccountEntity;
import com.rtravez.msa.exception.ExceptionManager;

/**
 * <b> Description de la class, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface AccountRepository extends BaseRepository<AccountEntity, Long> {

    /**
     * Find account by account number
     *
     * @param accountNumber
     * @return true or false
     * @throws ExceptionManager
     */
    Boolean exist(Long accountNumber) throws ExceptionManager;

    /**
     * Find account by account number
     * 
     * @param accountNumber
     * @return Object
     * @throws ExceptionManager
     */
    Optional<AccountEntity> findAccountByAccountNumber(Long accountNumber) throws ExceptionManager;

    Page<AccountEntity> findAllByStatusTrue(Pageable pageable) throws ExceptionManager;
}
