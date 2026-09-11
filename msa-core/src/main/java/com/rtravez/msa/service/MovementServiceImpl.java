package com.rtravez.msa.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rtravez.msa.dto.request.MovementRequest;
import com.rtravez.msa.dto.response.MovementReportResponse;
import com.rtravez.msa.dto.response.MovementResponse;
import com.rtravez.msa.entity.AccountEntity;
import com.rtravez.msa.entity.MovementEntity;
import com.rtravez.msa.exception.ExceptionManager;
import com.rtravez.msa.mapper.MovementMapper;
import com.rtravez.msa.repository.AccountRepository;
import com.rtravez.msa.repository.MovementRepository;
import com.rtravez.msa.util.DateUtil;
import com.rtravez.msa.web.ClientIpProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <b> Description de la class, interface or enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {

    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;
    private final MovementMapper movementMapper;
    private final ClientIpProvider clientIpProvider;

    /**
     * Find last movement
     *
     * @param account
     * @return
     */
    private BigDecimal getAvailableBalance(AccountEntity account) {
        return movementRepository.findLastMovement(account).map(MovementEntity::getAvailableBalance)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Save movement
     *
     * @param request
     * @param account
     * @return
     */
    private MovementEntity createMovement(MovementRequest request, AccountEntity account) {
        BigDecimal availableBalance = getAvailableBalance(account);
        MovementEntity movement = movementMapper.toEntity(request);

        BigDecimal newBalance = request.getMovementValue().doubleValue() > 0
                ? availableBalance.add(request.getMovementValue())
                : availableBalance.subtract(request.getMovementValue().abs());
        movement.setAvailableBalance(newBalance);
        movement.setAccount(accountRepository.findById(account.getAccountId())
                .orElseThrow(() -> new ExceptionManager.NotFoundException("La cuenta no existe")));
        movement.setCreatedHost(clientIpProvider.getCurrentIp());
        movement.setCreatedDate(DateUtil.currentDate());
        movement.setMovementDate(DateUtil.currentDate());
        movement.setStatus(request.getStatus());

        return movement;
    }

    /**
     * Validate balance available
     * 
     * @param account
     * @param value
     * @throws ExceptionManager.BalanceNotAvailableException
     */
    private void validateSufficientBalance(AccountEntity account, BigDecimal value)
            throws ExceptionManager.BalanceNotAvailableException {
        BigDecimal availableBalance = getAvailableBalance(account);
        if (value.signum() < 0 && value.abs().compareTo(availableBalance) > 0) {
            throw new ExceptionManager.BalanceNotAvailableException("Saldo no disponible");
        }
    }

    private void validateSufficientBalance(BigDecimal availableBalance, BigDecimal value)
            throws ExceptionManager.BalanceNotAvailableException {
        if (value.signum() < 0 && value.abs().compareTo(availableBalance) > 0) {
            throw new ExceptionManager.BalanceNotAvailableException("Saldo no disponible");
        }
    }

    private BigDecimal calculateBalance(BigDecimal availableBalance, BigDecimal value) {
        return value.signum() > 0
                ? availableBalance.add(value)
                : availableBalance.subtract(value.abs());
    }

    @Override
    @Transactional
    public MovementResponse processSaveMovement(MovementRequest request) throws ExceptionManager {
        try {
            Optional<AccountEntity> account = accountRepository.findAccountByAccountNumber(request.getAccountNumber());

            if (account.isPresent()) {
                validateSufficientBalance(account.get(), request.getMovementValue());
                MovementEntity movement = createMovement(request, account.get());
                return movementMapper.toResponse(movementRepository.save(movement));
            }
            return null;
        } catch (ExceptionManager.BalanceNotAvailableException e) {
            log.error("processSaveMovement", e);
            throw new ExceptionManager.BalanceNotAvailableException("Saldo no disponible");
        } catch (ExceptionManager e) {
            log.error("processSaveMovement", e);
            throw new ExceptionManager.GettingException("Error al guardar el registro");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovementReportResponse> findMovementByDateAndIdentification(LocalDateTime initialDate,
            LocalDateTime finalDate,
            String identification, String accountType) throws ExceptionManager {
        try {
            return movementRepository.findMovementByMovementDate(initialDate, finalDate, identification, accountType)
                    .stream()
                    .map(movementMapper::toReportResponse)
                    .toList();
        } catch (Exception e) {
            log.error("findMovementByDateAndIdentification: ", e);
            throw new ExceptionManager.FindingException("Error al buscar los registros");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean findMovementByAccountAccountId(Long accountId) throws ExceptionManager {
        try {
            return movementRepository.findMovementByAccountAccountId(accountId);
        } catch (Exception e) {
            log.error("findMovementByAccountAccountId: ", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }

    @Override
    @Transactional
    public int deleteMovementById(Long id) throws ExceptionManager {
        try {
            Optional<MovementEntity> movement = movementRepository.findById(id);

            if (movement.isPresent()) {
                MovementEntity movementEntity = movement.get();
                if (movementRepository.hasLaterActiveMovement(movementEntity.getAccount().getAccountId(),
                        movementEntity.getMovementDate(),
                        movementEntity.getMovementId())) {
                    throw new ExceptionManager.MovementDeletionException(
                            "No se puede eliminar un movimiento con movimientos posteriores");
                }
                movementEntity.setStatus(false);
                movementRepository.save(movementEntity);
                return 1;
            }
            return 0;
        } catch (ExceptionManager.MovementDeletionException e) {
            throw e;
        } catch (ExceptionManager e) {
            log.error("deleteMovementById", e);
            throw new ExceptionManager.DeletingException("Error al eliminar el registro");
        }
    }

    @Override
    @Transactional
    public MovementResponse processUpdateMovement(Long id, MovementRequest request) throws ExceptionManager {
        if (id == null || request == null) {
            throw new ExceptionManager.NotValidFieldException(
                    "El identificador y los datos del movimiento son obligatorios");
        }
        if (request.getMovementType() == null || request.getMovementType().isBlank()
                || !request.getMovementType().matches("(?i)[DR]")) {
            throw new ExceptionManager.NotValidFieldException("El tipo de movimiento debe ser D o R");
        }
        if (request.getMovementValue() == null || request.getMovementValue().signum() == 0) {
            throw new ExceptionManager.NotValidFieldException("El valor del movimiento no puede ser cero");
        }
        boolean debit = "D".equalsIgnoreCase(request.getMovementType());
        if (debit != (request.getMovementValue().signum() > 0)) {
            throw new ExceptionManager.NotValidFieldException(
                    "El tipo de movimiento no coincide con el signo del valor");
        }
        if (request.getAccountNumber() == null || request.getAccountNumber() <= 0) {
            throw new ExceptionManager.NotValidFieldException("La cuenta debe ser válida");
        }

        MovementEntity movement = movementRepository.findById(id)
                .filter(value -> Boolean.TRUE.equals(value.getStatus()))
                .orElseThrow(() -> new ExceptionManager.NotFoundException("El movimiento no existe"));
        AccountEntity account = movement.getAccount();

        if (!Objects.equals(account.getAccountNumber(), request.getAccountNumber())) {
            throw new ExceptionManager.NotValidFieldException("No se puede cambiar la cuenta del movimiento");
        }
        if (movementRepository.hasLaterActiveMovement(account.getAccountId(), movement.getMovementDate(), id)) {
            throw new ExceptionManager.MovementDeletionException(
                    "No se puede actualizar un movimiento con movimientos posteriores");
        }

        BigDecimal currentBalance = getAvailableBalance(account);
        BigDecimal balanceBeforeMovement = currentBalance.subtract(movement.getMovementValue());
        validateSufficientBalance(balanceBeforeMovement, request.getMovementValue());
        BigDecimal newBalance = calculateBalance(balanceBeforeMovement, request.getMovementValue());

        movement.setMovementType(Character.toUpperCase(request.getMovementType().charAt(0)));
        movement.setMovementValue(request.getMovementValue());
        movement.setAvailableBalance(newBalance);
        movement.setLastModifiedHost(clientIpProvider.getCurrentIp());
        movement.setLastModifiedDate(DateUtil.currentDate());
        return movementMapper.toResponse(movementRepository.save(movement));
    }
}
