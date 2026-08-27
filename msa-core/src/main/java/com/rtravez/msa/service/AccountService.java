package com.rtravez.msa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rtravez.msa.dto.request.AccountRequest;
import com.rtravez.msa.dto.request.MovementRequest;
import com.rtravez.msa.dto.request.UserRequest;
import com.rtravez.msa.dto.response.AccountResponse;
import com.rtravez.msa.dto.response.UserResponse;
import com.rtravez.msa.entity.AccountEntity;
import com.rtravez.msa.exception.ExceptionManager;
import com.rtravez.msa.repository.IAccountRepository;
import com.rtravez.msa.service.common.IDependenceService;
import com.rtravez.msa.util.DateUtil;
import com.rtravez.msa.web.ClientIpProvider;

import lombok.extern.slf4j.Slf4j;

/**
 * <b> Description de la class, interface or enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
@Service
@Slf4j
public class AccountService extends GenericService<AccountEntity, Long, IAccountRepository> implements IAccountService {

    @Autowired
    private IDependenceService dependenceService;
    @Autowired
    private IMovementService movementService;
    @Autowired
    private ClientIpProvider clientIpProvider;

    public AccountService(IAccountRepository repository) {
        super(repository);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean exist(Long accountNumber) throws ExceptionManager {
        try {
            return repository.exist(accountNumber);
        } catch (ExceptionManager e) {
            log.error("exist", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existUser(String identification) throws ExceptionManager {
        return isUserResponseValid(findUserResponse(identification));
    }

    @Override
    @Transactional
    public AccountResponse processSaveAccount(AccountRequest request) throws ExceptionManager {
        try {
            UserResponse userResponse = findUserResponse(request.getIdentification());
            if (isUserResponseValid(userResponse)) {
                AccountEntity account = createAccountEntity(request, userResponse);
                repository.save(account);
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
        return dependenceService.findUserByIdentification(userRequest);
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
                .personId(userResponse.getUserId())
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
        movementRequest.setValue(account.getInitialBalance());
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
                .personId(account.getPersonId())
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
    public List<AccountResponse> findAccountAll() throws ExceptionManager {
        try {
            List<AccountResponse> accountResponses = new ArrayList<>();
            List<AccountEntity> accounts = repository.findAll();
            accounts.forEach(it -> accountResponses.add(AccountResponse.builder()
                    .accountNumber(it.getAccountNumber())
                    .accountType(it.getAccountType())
                    .initialBalance(it.getInitialBalance())
                    .status(it.getStatus())
                    .accountId(it.getAccountId())
                    .personId(it.getPersonId())
                    .name(it.getPerson().getName())
                    .lastname(it.getPerson().getLastname())
                    .build()));
            return accountResponses;
        } catch (Exception e) {
            log.error("findAccountAll: ", e);
            throw new ExceptionManager.FindingException("Error al buscar los registros");
        }
    }

    @Override
    @Transactional
    public AccountResponse processUpdateAccount(AccountRequest request) throws ExceptionManager {
        try {
            UserRequest userRequest = UserRequest.builder().build();
            userRequest.setIdentification(request.getIdentification());

            // Consumir servicio web externos
            UserResponse userResponse = dependenceService.findUserByIdentification(userRequest);

            if (userResponse != null && userResponse.getUserId() != null) {
                Optional<AccountEntity> account = repository.findAccountByAccountNumber(request.getAccountNumber());

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
        super.update(account);
        this.processMovement(account);

        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .initialBalance(account.getInitialBalance())
                .status(account.getStatus())
                .name(userResponse.getName())
                .lastname(userResponse.getLastname())
                .accountId(account.getAccountId())
                .personId(account.getPersonId())
                .build();
    }

    @Override
    @Transactional
    public Long deleteAccountById(Long id) throws ExceptionManager {
        try {
            Optional<AccountEntity> account = repository.findById(id);

            if (account.isPresent()) {
                validateMovement(account.get().getAccountId());
                repository.deleteById(account.get().getAccountId());
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
        if (movementService.findMovementByAccountId(accountId)) {
            throw new ExceptionManager.ForeignException("Existen movimientos para esta cuenta");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountEntity> findAccountByAccountNumber(MovementRequest request) throws ExceptionManager {
        try {
            return repository.findAccountByAccountNumber(request.getAccountNumber());
        } catch (Exception e) {
            log.error("findAccountByAccountNumber", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }
}
