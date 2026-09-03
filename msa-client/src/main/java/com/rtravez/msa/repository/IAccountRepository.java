package com.rtravez.msa.repository;

import com.rtravez.msa.entity.AccountEntity;
import com.rtravez.msa.exception.ExceptionManager;

import java.util.Optional;

/**
 * <b> Description de la class, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface IAccountRepository extends IGenericRepository<AccountEntity, Long> {

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
     * @param accountNumber
     * @return Object
     * @throws ExceptionManager
     */
    Optional<AccountEntity> findAccountByAccountNumber(Long accountNumber) throws ExceptionManager;
}
