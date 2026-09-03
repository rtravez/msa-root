package com.rtravez.msa.service;

import com.rtravez.msa.dto.response.UserResponse;
import com.rtravez.msa.entity.view.UserView;
import com.rtravez.msa.exception.ExceptionManager;

import java.util.Optional;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface IUserService extends IGenericService<UserView, Long> {

    /**
     * Find user by username
     *
     * @param username
     * @return
     * @throws ExceptionManager
     */
    Optional<UserResponse> findByUsername(String username) throws ExceptionManager;

}
