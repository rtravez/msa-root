package com.rtravez.msa.service.common;

import com.rtravez.msa.dto.request.UserRequest;
import com.rtravez.msa.dto.response.UserResponse;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface IDependenceService {

    UserResponse findUserByIdentification(UserRequest request);
}
