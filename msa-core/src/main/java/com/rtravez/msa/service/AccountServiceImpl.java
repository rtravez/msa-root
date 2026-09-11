package com.rtravez.msa.service;

import com.rtravez.msa.dto.request.AccountRequest;
import com.rtravez.msa.dto.request.MovementRequest;
import com.rtravez.msa.dto.request.UserRequest;
import com.rtravez.msa.dto.response.AccountResponse;
import com.rtravez.msa.dto.response.UserResponse;
import com.rtravez.msa.entity.AccountEntity;
import com.rtravez.msa.exception.ExceptionManager;
import com.rtravez.msa.mapper.AccountMapper;
import com.rtravez.msa.repository.AccountRepository;
import com.rtravez.msa.repository.PersonRepository;
import com.rtravez.msa.util.DateUtil;
import com.rtravez.msa.web.ClientIpProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * <b> Description de la class, interface or enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserService userService;
    private final MovementService movementService;
    private final ClientIpProvider clientIpProvider;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PersonRepository personRepository;

    @Override
    @Transactional(readOnly = true)
    public Boolean exist(Long accountNumber) throws ExceptionManager {
        try {
            return accountRepository.exist(accountNumber);
        } catch (ExceptionManager e) {
            log.error("exist", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findUserByIdentification(String identification) throws ExceptionManager {
        return findUserResponse(identification);
    }

    @Override
    @Transactional
    public AccountResponse processSaveAccount(AccountRequest request) throws ExceptionManager {
        return processSaveAccount(request, findUserResponse(request.getIdentification()));
    }

    @Override
    @Transactional
    public AccountResponse processSaveAccount(AccountRequest request, UserResponse userResponse)
            throws ExceptionManager {
        try {
            if (isUserResponseValid(userResponse)) {
                AccountEntity account = createAccountEntity(request, userResponse);
                accountRepository.save(account);
                processMovement(account);
                return buildAccountResponse(account, userResponse);
            }
            return null;
        } catch (Exception e) {
            log.error("processSaveAccount", e);
            throw new ExceptionManager.GettingException("Error al guardar el registro");
        }
    }

    /**
     * Find user web service extern
     *
     * @param identification
     * @return
     * @throws ExceptionManager
     */
    private UserResponse findUserResponse(String identification) throws ExceptionManager {
        UserRequest userRequest = UserRequest.builder().build();
        userRequest.setIdentification(identification);
        return userService.findUserByIdentification(userRequest.getIdentification());
    }

    /**
     * User valid
     *
     * @param userResponse
     * @return
     */
    private boolean isUserResponseValid(UserResponse userResponse) {
        return userResponse != null && userResponse.getUserId() != null;
    }

    /**
     * Save account
     *
     * @param request
     * @param userResponse
     * @return
     */
    private AccountEntity createAccountEntity(AccountRequest request, UserResponse userResponse) {

        AccountEntity account = AccountEntity.builder()
                .accountNumber(request.getAccountNumber())
                .accountType(request.getAccountType())
                .initialBalance(request.getInitialBalance())
                .person(personRepository.findById(userResponse.getPersonId())
                        .orElseThrow(() -> new ExceptionManager("Person not found")))
                .build();

        account.setStatus(request.getStatus());
        account.setCreatedHost(clientIpProvider.getCurrentIp());

        return account;
    }

    /**
     * Process movement
     *
     * @param account
     * @throws ExceptionManager
     */
    private void processMovement(AccountEntity account) throws ExceptionManager {
        MovementRequest movementRequest = new MovementRequest();
        movementRequest.setAccountNumber(account.getAccountNumber());
        movementRequest.setMovementType("D");
        movementRequest.setMovementValue(account.getInitialBalance());
        movementService.processSaveMovement(movementRequest);
    }

    /**
     * AccountResponse
     *
     * @param account
     * @param userResponse
     * @return
     */
    private AccountResponse buildAccountResponse(AccountEntity account, UserResponse userResponse) {
        return AccountResponse.builder()
                .personId(account.getPerson().getPersonId())
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .initialBalance(account.getInitialBalance())
                .status(account.getStatus())
                .name(userResponse.getName())
                .lastname(userResponse.getLastname())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountResponse> findAccountAll(Pageable pageable) throws ExceptionManager {
        int pageSize = Math.min(pageable.getPageSize(), 100);
        Pageable boundedPageable = PageRequest.of(pageable.getPageNumber(), pageSize);
        return accountRepository.findAllByStatusTrue(boundedPageable).map(accountMapper::toResponse);
    }

    @Override
    @Transactional
    public AccountResponse processUpdateAccount(Long id, AccountRequest request) throws ExceptionManager {
        try {
            UserRequest userRequest = UserRequest.builder().build();
            userRequest.setIdentification(request.getIdentification());

            // Consumir servicio web externos
            UserResponse userResponse = userService.findUserByIdentification(userRequest.getIdentification());

            if (userResponse != null && userResponse.getUserId() != null) {
                Optional<AccountEntity> account = accountRepository
                        .findAccountByAccountNumber(request.getAccountNumber());

                return account.map(value -> this.updateAccount(value, userResponse, request)).orElse(null);
            }
            return null;
        } catch (Exception e) {
            log.error("processUpdateAccount", e);
            throw new ExceptionManager.GettingException("Error al actualizar el registro");
        }
    }

    /**
     * Update account
     *
     * @param account
     * @param userResponse
     * @param request
     * @return
     */
    private AccountResponse updateAccount(AccountEntity account, UserResponse userResponse,
            AccountRequest request) {
        account.setAccountNumber(request.getAccountNumber());
        account.setAccountType(request.getAccountType());
        account.setInitialBalance(request.getInitialBalance());

        account.setStatus(request.getStatus());
        account.setLastModifiedHost(clientIpProvider.getCurrentIp());
        account.setLastModifiedDate(DateUtil.currentDate());
        accountRepository.save(account);
        this.processMovement(account);

        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .initialBalance(account.getInitialBalance())
                .status(account.getStatus())
                .name(userResponse.getName())
                .lastname(userResponse.getLastname())
                .accountId(account.getAccountId())
                .personId(account.getPerson().getPersonId())
                .build();
    }

    @Override
    @Transactional
    public Long deleteAccountById(Long id) throws ExceptionManager {
        try {
            Optional<AccountEntity> account = accountRepository.findById(id);

            if (account.isPresent()) {
                validateMovement(account.get().getAccountId());
                accountRepository.deleteById(account.get().getAccountId());
                return 1L;
            }
            return 0L;
        } catch (ExceptionManager.ForeignException e) {
            log.error("deleteAccountById", e);
            throw new ExceptionManager.ForeignException("Existen movimientos para esta cuenta");
        } catch (ExceptionManager e) {
            log.error("deleteAccountById", e);
            throw new ExceptionManager.GettingException("Error al eliminar el registro");
        }
    }

    /**
     * Find movement by account id
     *
     * @param accountId
     * @throws ExceptionManager.ForeignException
     */
    private void validateMovement(Long accountId) throws ExceptionManager.ForeignException {
        if (movementService.findMovementByAccountAccountId(accountId)) {
            throw new ExceptionManager.ForeignException("Existen movimientos para esta cuenta");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountResponse> findAccountByAccountNumber(MovementRequest request) throws ExceptionManager {
        try {
            return accountRepository.findAccountByAccountNumber(request.getAccountNumber())
                    .map(accountMapper::toResponse);
        } catch (Exception e) {
            log.error("findAccountByAccountNumber", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse findAccountById(Long id) throws ExceptionManager {
        return accountRepository.findById(Objects.requireNonNull(id))
                .filter(value -> Boolean.TRUE.equals(value.getStatus()))
                .map(accountMapper::toResponse)
                .orElseThrow(() -> new ExceptionManager.NotFoundException("La cuenta no existe"));
    }
}
