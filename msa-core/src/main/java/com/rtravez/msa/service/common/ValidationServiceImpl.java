package com.rtravez.msa.service.common;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rtravez.msa.exception.ExceptionManager;
import com.rtravez.msa.util.ProjectUtil;

import lombok.RequiredArgsConstructor;

/**
 * <b> Description de la class, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
@Service
@Lazy
@RequiredArgsConstructor 
public class ValidationServiceImpl implements ValidationService {

	@Override
	@Transactional(readOnly = true)
	public boolean validationIdentification(String identification) throws ExceptionManager {
		return ProjectUtil.isCedulaValido(identification);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean validationRuc(String ruc) throws ExceptionManager {
		return ProjectUtil.isRucValido(ruc);
	}
}
