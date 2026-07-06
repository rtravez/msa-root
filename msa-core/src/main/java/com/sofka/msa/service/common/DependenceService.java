package com.sofka.msa.service.common;

import com.sofka.msa.config.UrlDependenceWebServices;
import com.sofka.msa.dto.request.CustomerRequest;
import com.sofka.msa.dto.response.CustomerResponse;
import com.sofka.msa.util.EnvironmentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class DependenceService implements IDependenceService {

    @Autowired
    private UrlDependenceWebServices url;

    @Autowired
    @Qualifier("restTemplateMcpServices")
    private RestTemplate restTemplateMcpServices;

    @Override
    public CustomerResponse findCustomerByIdentification(CustomerRequest request) {
        try {
            String path = EnvironmentUtil.getDomainNameContext(url.getFindCustomerByIdentification());
            ResponseEntity<CustomerResponse> response = restTemplateMcpServices.postForEntity(path, request, CustomerResponse.class);
            return response.getBody();
        } catch (RestClientException e) {
            log.error("Ha ocurrido un error al obtener el cliente por identificación =>", e);
            throw e;
        }
    }
}
