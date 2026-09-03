package com.rtravez.msa.repository;

import com.rtravez.msa.entity.AccountEntity;
import com.rtravez.msa.exception.ExceptionManager;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import jakarta.persistence.NoResultException;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;
import java.util.Optional;

import static com.rtravez.msa.entity.QAccountEntity.accountEntity;
import static com.rtravez.msa.entity.view.QPersonView.personView;

@Slf4j
@Repository
public class AccountRepository extends GenericRepository<AccountEntity, Long> implements IAccountRepository {

    public AccountRepository() {
        super(AccountEntity.class);
    }

    @Override
    public Boolean exist(Long accountNumber) throws ExceptionManager {
        try {
            BooleanBuilder where = new BooleanBuilder();
            where.and(accountEntity.accountNumber.eq(accountNumber));
            where.and(accountEntity.status.isTrue());

            JPQLQuery<Long> query = queryFactory.selectFrom(accountEntity).select(accountEntity.accountNumber)
                    .innerJoin(accountEntity.person, personView)
                    .where(where);
            return query.fetchFirst() != null;
        } catch (ExceptionManager e) {
            log.error("exist: ", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }

    }

    @Override
    public Optional<AccountEntity> findAccountByAccountNumber(Long accountNumber) throws ExceptionManager {
        try {
            /*
             * BooleanBuilder where = new BooleanBuilder();
             * where.and(accountEntity.accountNumber.eq(accountNumber));
             * where.and(accountEntity.status.isTrue());
             * 
             * return Optional.ofNullable(queryFactory.selectFrom(accountEntity)
             * .innerJoin(accountEntity.user, userView)
             * .where(where).fetchFirst());
             */

            String jpql = "SELECT a FROM " + AccountEntity.class.getName()
                    + " a WHERE a.accountNumber = :accountNumber AND a.status = true";

            TypedQuery<AccountEntity> query = getEntityManager().createQuery(jpql, AccountEntity.class);
            query.setParameter("accountNumber", accountNumber);
            query.setLockMode(LockModeType.PESSIMISTIC_WRITE);

            AccountEntity result = query.getSingleResult();
            return Optional.ofNullable(result);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            log.error("findAccountByAccountNumber: ", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }
}
