package com.rtravez.msa.repository;

import com.rtravez.msa.dto.response.MovementReportResponse;
import com.rtravez.msa.entity.AccountEntity;
import com.rtravez.msa.entity.MovementEntity;
import com.rtravez.msa.exception.ExceptionManager;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.sql.SQLExpressions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import static com.rtravez.msa.entity.QAccountEntity.accountEntity;
import static com.rtravez.msa.entity.QMovementEntity.movementEntity;
import static com.rtravez.msa.entity.view.QPersonView.personView;
import static com.rtravez.msa.util.DateUtil.convertStringToDate;
import static com.querydsl.core.types.Projections.bean;

@Slf4j
@Repository
public class MovementRepositoryImpl extends BaseRepositoryImpl<MovementEntity, Long> implements MovementRepository {

    public MovementRepositoryImpl(EntityManager em) {
        super(MovementEntity.class, em);
    }

    @Override
    public Optional<MovementEntity> findLastMovement(AccountEntity account) throws ExceptionManager {
        try {
            String jpql = "SELECT m FROM " + MovementEntity.class.getName()
                    + " m WHERE m.account.accountId = :accountId AND m.status = :status ORDER BY m.movementDate DESC";

            TypedQuery<MovementEntity> query = entityManager.createQuery(jpql, MovementEntity.class);
            query.setParameter("accountId", account.getAccountId());
            query.setParameter("status", true);
            query.setMaxResults(1);

            MovementEntity result = query.getSingleResult();
            return Optional.ofNullable(result);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            log.error("findLastMovement: ", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }

    @Override
    public List<MovementEntity> findMovementByMovementDate(LocalDateTime initialDate, LocalDateTime finalDate,
                                                                   String identification, String accountType) throws ExceptionManager {
        try {
            BooleanBuilder where = new BooleanBuilder();
            where.and(movementEntity.movementDate.between(initialDate, finalDate));
            where.and(movementEntity.status.isTrue());

            if (StringUtils.hasText(identification)) {
                where.and(personView.identification.eq(identification));
            }

            if (StringUtils.hasText(accountType)) {
                where.and(accountEntity.accountType.eq(accountType));
            }

            return queryFactory.selectFrom(movementEntity)
                    .select(movementEntity)
                    .innerJoin(movementEntity.account, accountEntity)
                    .innerJoin(accountEntity.person, personView)
                    .where(where).orderBy(personView.identification.asc(), accountEntity.accountType.asc(), movementEntity.movementDate.desc())
                    .fetch();
        } catch (Exception e) {
            log.error("findMovementByMovementDate: ", e);
            throw new ExceptionManager.FindingException("Error al buscar los registros");
        }
    }

    @Override
    public boolean findMovementByAccountAccountId(Long accountId) throws ExceptionManager {
        try {
            BooleanBuilder where = new BooleanBuilder();
            where.and(movementEntity.account.accountId.eq(accountId));

            JPQLQuery<String> query = queryFactory.selectFrom(movementEntity)
                    .select(movementEntity.movementId.stringValue())
                    .innerJoin(movementEntity.account, accountEntity)
                    .where(where);
            return org.apache.commons.lang3.StringUtils.isNotBlank(query.fetchFirst());
        } catch (ExceptionManager e) {
            log.error("findMovementByAccountAccountId: ", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }

    }

    @Override
    public boolean hasLaterActiveMovement(Long accountId, LocalDateTime movementDate, Long movementId)
            throws ExceptionManager {
        try {
            String jpql = "SELECT COUNT(a) FROM " + MovementEntity.class.getName()
                    + " a WHERE a.accountId = :accountId AND a.status = true"
                    + " AND (a.movementDate > :movementDate"
                    + " OR (a.movementDate = :movementDate AND a.movementId > :movementId))";

            TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
            query.setParameter("accountId", accountId);
            query.setParameter("movementDate", movementDate);
            query.setParameter("movementId", movementId);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            log.error("hasLaterActiveMovement: ", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }
}
