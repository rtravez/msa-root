package com.sofka.msa.repository;

import com.sofka.msa.dto.response.MovementReportResponse;
import com.sofka.msa.entity.AccountEntity;
import com.sofka.msa.entity.MovementEntity;
import com.sofka.msa.exception.ExceptionManager;

import java.util.List;
import java.util.Optional;

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
}
