package com.sofka.msa.service.common;

import com.sofka.msa.exception.ExceptionManager;
import com.sofka.msa.util.ProjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b> Description de la class, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
@Service
@Lazy
@Slf4j
public class ValidationService implements IValidationService {

	@Override
	@Transactional(readOnly = true)
	public boolean validationIdentification(String identification) throws ExceptionManager {
		try {
            log.info("validationIdentification: {}", ProjectUtil.isCedulaValido(identification));
			return ProjectUtil.isCedulaValido(identification);
		} catch (ExceptionManager e) {
			log.error("validationIdentification: ", e);
			throw new ExceptionManager.NotValidFieldException("Error al validar la identificación");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public boolean validationRuc(String ruc) throws ExceptionManager {
		try {
			return ProjectUtil.isRucValido(ruc);
		} catch (ExceptionManager e) {
			log.error("validationRuc: ", e);
			throw new ExceptionManager.NotValidFieldException("Error al validar el ruc");
		}
	}
}
