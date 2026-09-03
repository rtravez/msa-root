package com.rtravez.msa.service;

import com.rtravez.msa.dto.request.AccountRequest;
import com.rtravez.msa.dto.request.MovementRequest;
import com.rtravez.msa.dto.response.AccountResponse;
import com.rtravez.msa.dto.response.UserResponse;
import com.rtravez.msa.entity.AccountEntity;
import com.rtravez.msa.exception.ExceptionManager;

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
     * Find user by identification
     *
     * @param identification
     * @return user response
     * @throws ExceptionManager
     */
    UserResponse findUserByIdentification(String identification) throws ExceptionManager;

    /**
     * Process save account
     *
     * @param request
     * @return
     * @throws ExceptionManager
     */
    AccountResponse processSaveAccount(AccountRequest request) throws ExceptionManager;

    /**
     * Process save account with the previously found user
     *
     * @param request
     * @param userResponse
     * @return
     * @throws ExceptionManager
     */
    AccountResponse processSaveAccount(AccountRequest request, UserResponse userResponse) throws ExceptionManager;

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
