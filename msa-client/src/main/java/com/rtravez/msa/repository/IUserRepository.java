package com.rtravez.msa.repository;

import com.rtravez.msa.entity.view.UserView;
import com.rtravez.msa.exception.ExceptionManager;

import java.util.Optional;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface IUserRepository extends IGenericRepository<UserView, Long> {

    /**
     * Find User by username
     *
     * @param username
     * @return
     * @throws ExceptionManager
     */
    Optional<UserView> findByUsername(String username) throws ExceptionManager;

}
