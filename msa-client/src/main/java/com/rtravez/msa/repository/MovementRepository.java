package com.rtravez.msa.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rtravez.msa.entity.AccountEntity;
import com.rtravez.msa.entity.MovementEntity;
import com.rtravez.msa.exception.ExceptionManager;

/**
 * <b> Description de la class, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface MovementRepository extends BaseRepository<MovementEntity, Long> {

    Page<MovementEntity> findAllByStatusTrue(Pageable pageable) throws ExceptionManager;

    Optional<MovementEntity> findLastMovement(AccountEntity account) throws ExceptionManager;

    List<MovementEntity> findMovementByMovementDate(LocalDateTime initialDate, LocalDateTime finalDate, String identification, String accountType) throws ExceptionManager;

    boolean findMovementByAccountAccountId(Long accountId) throws ExceptionManager;

    boolean hasLaterActiveMovement(Long accountId, LocalDateTime movementDate, Long movementId) throws ExceptionManager;
}
