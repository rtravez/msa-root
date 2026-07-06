package com.sofka.msa.repository;

import com.sofka.msa.entity.view.CustomerView;
import com.sofka.msa.exception.ExceptionManager;

import java.util.Optional;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface ICustomerRepository extends IGenericRepository<CustomerView, Long> {

    /**
     * Find Customer by username
     *
     * @param username
     * @return
     * @throws ExceptionManager
     */
    Optional<CustomerView> findCustomerByUsername(String username) throws ExceptionManager;

}
