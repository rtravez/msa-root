package com.sofka.msa.repository;

import com.sofka.msa.entity.view.CustomerView;
import com.sofka.msa.exception.ExceptionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.sofka.msa.entity.view.QCustomerView.customerView;
import static com.sofka.msa.entity.view.QPersonView.personView;


@Slf4j
@Repository
public class CustomerRepository extends GenericRepository<CustomerView, Long> implements ICustomerRepository {

    /**
     * Constructor
     */
    public CustomerRepository() {
        super(CustomerView.class);
    }

    @Override
    public Optional<CustomerView> findCustomerByUsername(String username) throws ExceptionManager {
        try {
            return Optional.ofNullable(queryFactory.selectFrom(customerView).innerJoin(customerView.person, personView)
                    .fetchJoin().where(customerView.username.eq(username).and(customerView.status.isTrue())).fetchFirst());
        } catch (Exception e) {
            log.error("findUserByUsername: ", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }


}
