package com.rtravez.msa.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.rtravez.msa.entity.AccountEntity;
import com.rtravez.msa.entity.MovementEntity;
import com.rtravez.msa.exception.ExceptionManager;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.rtravez.msa.entity.QAccountEntity.accountEntity;
import static com.rtravez.msa.entity.QMovementEntity.movementEntity;
import static com.rtravez.msa.entity.view.QPersonView.personView;

@Repository
public class MovementRepositoryImpl extends BaseRepositoryImpl<MovementEntity, Long> implements MovementRepository {

    public MovementRepositoryImpl(EntityManager em) {
        super(MovementEntity.class, em);
    }

    @Override
    public Page<MovementEntity> findAllByStatusTrue(Pageable pageable) throws ExceptionManager {
        BooleanBuilder where = new BooleanBuilder();
        where.and(movementEntity.status.isTrue());

        List<MovementEntity> movements = queryFactory.selectFrom(movementEntity)
                .where(where)
                .orderBy(movementEntity.movementDate.desc(), movementEntity.movementId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory.select(movementEntity.count())
                .from(movementEntity)
                .where(where)
                .fetchOne();
        return new PageImpl<>(movements, pageable, total);
    }

    @Override
    public Optional<MovementEntity> findLastMovement(AccountEntity account) throws ExceptionManager {
        BooleanBuilder where = new BooleanBuilder();
        where.and(movementEntity.account.accountId.eq(account.getAccountId()));
        where.and(movementEntity.status.isTrue());

        return Optional.ofNullable(queryFactory.selectFrom(movementEntity)
                .where(where).orderBy(movementEntity.movementDate.desc())
                .fetchFirst());
    }

    @Override
    public Page<MovementEntity> findMovementByMovementDate(LocalDateTime initialDate, LocalDateTime finalDate,
            String identification, String accountType, Pageable pageable) throws ExceptionManager {
        BooleanBuilder where = new BooleanBuilder();
        where.and(movementEntity.movementDate.between(initialDate, finalDate));
        where.and(movementEntity.status.isTrue());
        where.and(personView.identification.eq(identification));
        where.and(accountEntity.accountType.eq(accountType));

        List<MovementEntity> movements = queryFactory.selectFrom(movementEntity)
            .select(movementEntity)
            .innerJoin(movementEntity.account, accountEntity)
            .innerJoin(accountEntity.person, personView)
            .where(where)
            .orderBy(personView.identification.asc(), accountEntity.accountType.asc(),
                movementEntity.movementDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory.select(movementEntity.count())
            .from(movementEntity)
            .innerJoin(movementEntity.account, accountEntity)
            .innerJoin(accountEntity.person, personView)
            .where(where)
            .fetchOne();
        return new PageImpl<>(movements, pageable, total == null ? 0 : total);
    }

    @Override
    public boolean findMovementByAccountAccountId(Long accountId) throws ExceptionManager {
        BooleanBuilder where = new BooleanBuilder();
        where.and(movementEntity.account.accountId.eq(accountId));

        JPQLQuery<String> query = queryFactory.selectFrom(movementEntity)
                .select(movementEntity.movementId.stringValue())
                .innerJoin(movementEntity.account, accountEntity)
                .where(where);
        return org.apache.commons.lang3.StringUtils.isNotBlank(query.fetchFirst());

    }

    @Override
    public boolean hasLaterActiveMovement(Long accountId, LocalDateTime movementDate, Long movementId)
            throws ExceptionManager {
        BooleanBuilder where = new BooleanBuilder();
        where.and(movementEntity.account.accountId.eq(accountId));
        where.and(movementEntity.status.isTrue());
        where.and(movementEntity.movementDate.gt(movementDate)
            .or(movementEntity.movementDate.eq(movementDate)
                .and(movementEntity.movementId.gt(movementId))));

        Long count = queryFactory
            .select(movementEntity.count())
            .from(movementEntity)
            .where(where)
            .fetchOne();
        return count != null && count > 0;
    }
}
