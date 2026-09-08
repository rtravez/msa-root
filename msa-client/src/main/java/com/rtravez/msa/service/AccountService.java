package com.rtravez.msa.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rtravez.msa.dto.request.AccountRequest;
import com.rtravez.msa.dto.request.MovementRequest;
import com.rtravez.msa.dto.response.AccountResponse;
import com.rtravez.msa.dto.response.UserResponse;
import com.rtravez.msa.entity.AccountEntity;
import com.rtravez.msa.exception.ExceptionManager;

/**
 * <b> Description de la class, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface AccountService {

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
     * @param pageable
     * @return
     * @throws ExceptionManager
     */
    Page<AccountResponse> findAccountAll(Pageable pageable) throws ExceptionManager;

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
