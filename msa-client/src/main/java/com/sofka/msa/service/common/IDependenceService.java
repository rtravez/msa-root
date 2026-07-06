package com.sofka.msa.service.common;

import com.sofka.msa.dto.request.CustomerRequest;
import com.sofka.msa.dto.response.CustomerResponse;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface IDependenceService {

    CustomerResponse findCustomerByIdentification(CustomerRequest request);
}
