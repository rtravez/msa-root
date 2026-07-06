package com.sofka.msa.repository;

import com.sofka.msa.entity.AccountEntity;
import com.sofka.msa.exception.ExceptionManager;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Optional;

import static com.sofka.msa.entity.QAccountEntity.accountEntity;
import static com.sofka.msa.entity.view.QCustomerView.customerView;

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
                    .innerJoin(accountEntity.customer, customerView)
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
            /*BooleanBuilder where = new BooleanBuilder();
            where.and(accountEntity.accountNumber.eq(accountNumber));
            where.and(accountEntity.status.isTrue());

            return Optional.ofNullable(queryFactory.selectFrom(accountEntity)
                    .innerJoin(accountEntity.customer, customerView)
                    .where(where).fetchFirst());*/

            String jpql = "SELECT a FROM " + AccountEntity.class.getName() + " a WHERE a.accountNumber = :accountNumber AND a.status = true";

            TypedQuery<AccountEntity> query = getEntityManager().createQuery(jpql, AccountEntity.class);
            query.setParameter("accountNumber", accountNumber);

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
