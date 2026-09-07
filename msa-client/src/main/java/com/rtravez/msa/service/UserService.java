package com.rtravez.msa.service;

import java.util.Optional;

import com.rtravez.msa.dto.response.UserResponse;
import com.rtravez.msa.exception.ExceptionManager;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface UserService {

    /**
     * Find user by username
     *
     * @param username
     * @return
     * @throws ExceptionManager
     */
    Optional<UserResponse> findByUsername(String username) throws ExceptionManager;

}
