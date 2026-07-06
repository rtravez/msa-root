package com.sofka.msa.service;

import com.sofka.msa.dto.request.AccountRequest;
import com.sofka.msa.dto.request.MovementRequest;
import com.sofka.msa.dto.response.AccountResponse;
import com.sofka.msa.entity.AccountEntity;
import com.sofka.msa.exception.ExceptionManager;

import java.util.List;
import java.util.Optional;

/**
 * <b> Description de la class, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface IAccountService extends IGenericService<AccountEntity, Long> {

    /**
     * Find account by account number
     *
     * @param accountNumber
     * @return true or false
     * @throws ExceptionManager
     */
    Boolean exist(Long accountNumber) throws ExceptionManager;

    /**
     * Process save account
     *
     * @param request
     * @return
     * @throws ExceptionManager
     */
    AccountResponse processSaveAccount(AccountRequest request) throws ExceptionManager;

    /**
     * Find account all
     *
     * @return
     * @throws ExceptionManager
     */
    List<AccountResponse> findAccountAll() throws ExceptionManager;

    /**
     * Process update account
     *
     * @param request
     * @return
     * @throws ExceptionManager
     */
    AccountResponse processUpdateAccount(AccountRequest request) throws ExceptionManager;

    /**
     * Delete account by id
     *
     * @param id
     * @return
     * @throws ExceptionManager
     */
    Long deleteAccountById(Long id) throws ExceptionManager;

    /**
     * Find account by account number
     *
     * @param request
     * @return Object
     * @throws ExceptionManager
     */
    Optional<AccountEntity> findAccountByAccountNumber(MovementRequest request) throws ExceptionManager;
}
