package com.rtravez.msa.repository;

import com.rtravez.msa.dto.response.MovementReportResponse;
import com.rtravez.msa.entity.AccountEntity;
import com.rtravez.msa.entity.MovementEntity;
import com.rtravez.msa.exception.ExceptionManager;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

/**
 * <b> Description de la class, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface IMovementRepository extends IGenericRepository<MovementEntity, Long> {

    Optional<MovementEntity> findLastMovement(AccountEntity account) throws ExceptionManager;

    List<MovementReportResponse> findMovementByDateAndIdentification(String initialDate, String finalDate, String identification, String accountType) throws ExceptionManager;

    boolean findMovementByAccountId(Long accountId) throws ExceptionManager;

    boolean hasLaterActiveMovement(Long accountId, LocalDateTime movementDate, Long movementId) throws ExceptionManager;
}
