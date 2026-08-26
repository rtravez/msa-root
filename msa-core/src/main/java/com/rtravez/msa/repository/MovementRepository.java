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

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

import static com.rtravez.msa.entity.QAccountEntity.accountEntity;
import static com.rtravez.msa.entity.QMovementEntity.movementEntity;
import static com.rtravez.msa.entity.view.QCustomerView.customerView;
import static com.rtravez.msa.entity.view.QPersonView.personView;
import static com.rtravez.msa.util.DateUtil.convertStringToDate;
import static com.querydsl.core.types.Projections.bean;

@Slf4j
@Repository
public class MovementRepository extends GenericRepository<MovementEntity, Long> implements IMovementRepository {

    public MovementRepository() {
        super(MovementEntity.class);
    }

    @Override
    public Optional<MovementEntity> findLastMovement(AccountEntity account) throws ExceptionManager {
        try {
            String jpql = "SELECT a FROM " + MovementEntity.class.getName() + " a WHERE a.accountId = :accountId AND a.status = :status ORDER BY a.movementDate DESC";

            TypedQuery<MovementEntity> query = getEntityManager().createQuery(jpql, MovementEntity.class);
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
    public List<MovementReportResponse> findMovementByDateAndIdentification(String initialDate, String finalDate, String identification, String accountType) throws ExceptionManager {
        try {
            BooleanBuilder where = new BooleanBuilder();
            where.and(personView.identification.eq(identification));
            where.and(SQLExpressions.date(movementEntity.movementDate)
                    .between(convertStringToDate(initialDate), convertStringToDate(finalDate)));
            where.and(movementEntity.status.isTrue());

            if(StringUtils.hasText(accountType)){
                where.and(accountEntity.accountType.eq(accountType));
            }

            return queryFactory.selectFrom(movementEntity)
                    .select(bean(MovementReportResponse.class, movementEntity.movementDate, personView.identification,
                            personView.name, personView.lastname, accountEntity.accountNumber, accountEntity.accountType,
                            accountEntity.initialBalance, movementEntity.status, movementEntity.value,
                            movementEntity.availableBalance))
                    .innerJoin(movementEntity.account, accountEntity)
                    .innerJoin(accountEntity.customer, customerView)
                    .innerJoin(customerView.person, personView)
                    .where(where).orderBy(movementEntity.movementDate.desc())
                    .fetch();
        } catch (Exception e) {
            log.error("findMovementByDateAndIdentification: ", e);
            throw new ExceptionManager.FindingException("Error al buscar los registros");
        }
    }

    @Override
    public boolean findMovementByAccountId(Long accountId) throws ExceptionManager {
        try {
            BooleanBuilder where = new BooleanBuilder();
            where.and(movementEntity.accountId.eq(accountId));

            JPQLQuery<String> query = queryFactory.selectFrom(movementEntity).select(movementEntity.movementId.stringValue())
                    .innerJoin(movementEntity.account, accountEntity)
                    .where(where);
            return org.apache.commons.lang3.StringUtils.isNotBlank(query.fetchFirst());
        } catch (ExceptionManager e) {
            log.error("findMovementByAccountId: ", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }

    }
}
