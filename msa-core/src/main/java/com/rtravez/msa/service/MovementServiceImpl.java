package com.rtravez.msa.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rtravez.msa.dto.request.MovementRequest;
import com.rtravez.msa.mapper.MovementMapper;
import com.rtravez.msa.dto.response.MovementReportResponse;
import com.rtravez.msa.dto.response.MovementResponse;
import com.rtravez.msa.entity.AccountEntity;
import com.rtravez.msa.entity.MovementEntity;
import com.rtravez.msa.exception.ExceptionManager;
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
        movement.setAccount(account);
        movement.setCreatedHost(clientIpProvider.getCurrentIp());
        movement.setCreatedDate(DateUtil.currentDate());
        movement.setMovementDate(DateUtil.currentDate());
        movement.setStatus(request.getStatus());

        return movement;
    }

    /**
     * MovementResponse
     *
     * @param movement
     * @return
     */
    private MovementResponse buildMomentResponse(MovementEntity movement) {
        return MovementResponse.builder()
                .movementId(movement.getMovementId())
                .accountId(movement.getAccount().getAccountId())
                .movementDate(movement.getMovementDate())
                .movementType(movement.getMovementType())
                .movementValue(movement.getMovementValue())
                .availableBalance(movement.getAvailableBalance())
                .build();
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

    @Override
    @Transactional
    public MovementResponse processSaveMovement(MovementRequest request) throws ExceptionManager {
        try {
            Optional<AccountEntity> account = accountRepository.findAccountByAccountNumber(request.getAccountNumber());

            if (account.isPresent()) {
                validateSufficientBalance(account.get(), request.getMovementValue());
                MovementEntity movement = createMovement(request, account.get());
                movementRepository.save(movement);
                return buildMomentResponse(movement);
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
    public List<MovementReportResponse> findMovementByDateAndIdentification(String initialDate, String finalDate,
            String identification, String accountType) throws ExceptionManager {
        try {
            return movementRepository.findMovementByDateAndIdentification(initialDate, finalDate, identification, accountType);
        } catch (Exception e) {
            log.error("findMovementByDateAndIdentification: ", e);
            throw new ExceptionManager.FindingException("Error al buscar los registros");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean findMovementByAccountId(Long accountId) throws ExceptionManager {
        try {
            return movementRepository.findMovementByAccountId(accountId);
        } catch (Exception e) {
            log.error("findMovementByAccountId: ", e);
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
                if (movementRepository.hasLaterActiveMovement(movementEntity.getAccount().getAccountId(), movementEntity.getMovementDate(),
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
}
