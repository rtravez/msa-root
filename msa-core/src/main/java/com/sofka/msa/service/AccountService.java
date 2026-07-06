package com.sofka.msa.service;

import com.sofka.msa.dto.request.AccountRequest;
import com.sofka.msa.dto.request.CustomerRequest;
import com.sofka.msa.dto.request.MovementRequest;
import com.sofka.msa.dto.response.AccountResponse;
import com.sofka.msa.dto.response.CustomerResponse;
import com.sofka.msa.entity.AccountEntity;
import com.sofka.msa.exception.ExceptionManager;
import com.sofka.msa.repository.IAccountRepository;
import com.sofka.msa.service.common.IDependenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.sofka.msa.common.Constants.CREATION_HOST;
import static com.sofka.msa.common.Constants.CREATION_USER;
import static com.sofka.msa.common.Constants.MODIFICATION_HOST;
import static com.sofka.msa.common.Constants.MODIFICATION_USER;

/**
 * <b> Description de la class, interface o enumeration. </b>
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


    public AccountService(IAccountRepository repository) {
        super(repository);
    }


    @Override
    @Transactional(readOnly = true)
    public Boolean exist(Long accountNumber) throws ExceptionManager {
        try {
            return repository.exist(accountNumber);
        } catch (ExceptionManager e) {
            log.error("exist: {0}", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }

    @Override
    @Transactional
    public AccountResponse processSaveAccount(AccountRequest request) throws ExceptionManager {
        try {
            CustomerResponse customerResponse = findCustomerResponse(request.getIdentification());
            if (isCustomerResponseValid(customerResponse)) {
                AccountEntity account = createAccountEntity(request, customerResponse);
                repository.save(account);
                processMovement(account);
                return buildAccountResponse(account, customerResponse);
            }
            return null;
        } catch (Exception e) {
            log.error("processSaveAccount: {0}", e);
            throw new ExceptionManager.GettingException("Error al guardar el registro");
        }
    }

    /**
     * Find customer web service extern
     *
     * @param identification
     * @return
     * @throws ExceptionManager
     */
    private CustomerResponse findCustomerResponse(String identification) throws ExceptionManager {
        CustomerRequest customerRequest = CustomerRequest.builder().build();
        customerRequest.setIdentification(identification);
        return dependenceService.findCustomerByIdentification(customerRequest);
    }

    /**
     * Customer valid
     *
     * @param customerResponse
     * @return
     */
    private boolean isCustomerResponseValid(CustomerResponse customerResponse) {
        return customerResponse != null && customerResponse.getCustomerId() != null;
    }

    /**
     * Save account
     *
     * @param request
     * @param customerResponse
     * @return
     */
    private AccountEntity createAccountEntity(AccountRequest request, CustomerResponse customerResponse) {

        AccountEntity account = AccountEntity.builder()
                .accountNumber(request.getAccountNumber())
                .accountType(request.getAccountType())
                .initialBalance(request.getInitialBalance())
                .customerId(customerResponse.getCustomerId())
                .build();

        account.setStatus(request.getStatus());
        account.setCreationUser(CREATION_USER);
        account.setCreationHost(CREATION_HOST);
        account.setCreationDate(Date.from(Instant.now()));

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
     * @param customerResponse
     * @return
     */
    private AccountResponse buildAccountResponse(AccountEntity account, CustomerResponse customerResponse) {
        return AccountResponse.builder()
                .customerId(account.getCustomerId())
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .initialBalance(account.getInitialBalance())
                .status(account.getStatus())
                .name(customerResponse.getName())
                .lastname(customerResponse.getLastname())
                .build();
    }

    @Override
    public List<AccountResponse> findAccountAll() throws ExceptionManager {
        try {
            List<AccountResponse> accountResponses = new ArrayList<>();
            List<AccountEntity> customers = repository.findAll();
            customers.forEach(it -> accountResponses.add(AccountResponse.builder()
                    .accountNumber(it.getAccountNumber())
                    .accountType(it.getAccountType())
                    .initialBalance(it.getInitialBalance())
                    .status(it.getStatus())
                    .accountId(it.getAccountId())
                    .customerId(it.getCustomerId())
                    .name(it.getCustomer().getPerson().getName())
                    .lastname(it.getCustomer().getPerson().getLastname())
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
            CustomerRequest customerRequest = CustomerRequest.builder().build();
            customerRequest.setIdentification(request.getIdentification());

            //Consumir servicio web externos
            CustomerResponse customerResponse = dependenceService.findCustomerByIdentification(customerRequest);

            if (customerResponse != null && customerResponse.getCustomerId() != null) {
                Optional<AccountEntity> account = repository.findAccountByAccountNumber(request.getAccountNumber());

                return account.map(value -> this.updateAccount(value, customerResponse, request)).orElse(null);
            }
            return null;
        } catch (Exception e) {
            log.error("processUpdateAccount: {0}", e);
            throw new ExceptionManager.GettingException("Error al actualizar el registro");
        }
    }

    /**
     * Update account
     *
     * @param account
     * @param customerResponse
     * @param request
     * @return
     */
    private AccountResponse updateAccount(AccountEntity account, CustomerResponse customerResponse, AccountRequest request) {
        account.setAccountNumber(request.getAccountNumber());
        account.setAccountType(request.getAccountType());
        account.setInitialBalance(request.getInitialBalance());

        account.setStatus(request.getStatus());
        account.setModificationUser(MODIFICATION_USER);
        account.setModificationHost(MODIFICATION_HOST);
        account.setModificationDate(Date.from(Instant.now()));
        super.update(account);
        this.processMovement(account);

        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .initialBalance(account.getInitialBalance())
                .status(account.getStatus())
                .name(customerResponse.getName())
                .lastname(customerResponse.getLastname())
                .accountId(account.getAccountId())
                .customerId(account.getCustomerId())
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
            log.error("deleteAccountById: {0}", e);
            throw new ExceptionManager.ForeignException("Existen movimientos para esta cuenta");
        } catch (ExceptionManager e) {
            log.error("deleteAccountById: {0}", e);
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
            log.error("findAccountByAccountNumber: {0}", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }
}
