package com.rtravez.msa.service;

import com.rtravez.msa.entity.view.CustomerView;
import com.rtravez.msa.exception.ExceptionManager;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface ICustomerService extends IGenericService<CustomerView, Long>, UserDetailsService {

    /**
     * Find customer by username
     *
     * @param username
     * @return
     * @throws ExceptionManager
     */
    Optional<CustomerView> findCustomerByUsername(String username) throws ExceptionManager;

}
