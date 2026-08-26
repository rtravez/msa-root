package com.rtravez.msa.service.common;

import com.rtravez.msa.exception.ExceptionManager;

/**
 * <b> Description de la class, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface IValidationService {

	boolean validationIdentification(String identification) throws ExceptionManager;

	boolean validationRuc(String ruc) throws ExceptionManager;
}
