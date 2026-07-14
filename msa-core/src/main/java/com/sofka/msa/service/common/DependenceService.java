package com.sofka.msa.service.common;

import com.sofka.msa.config.UrlDependenceWebServices;
import com.sofka.msa.dto.request.CustomerRequest;
import com.sofka.msa.dto.response.CustomerResponse;
import com.sofka.msa.util.EnvironmentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Service
@Slf4j
public class DependenceService implements IDependenceService {

    @Autowired
    private UrlDependenceWebServices url;

    @Autowired
    @Qualifier("webClientMcpServices")
    private WebClient webClientMcpServices;

    @Override
    public CustomerResponse findCustomerByIdentification(CustomerRequest request) {
        try {
            String path = EnvironmentUtil.getDomainNameContext(url.getFindCustomerByIdentification());
            return webClientMcpServices.post()
                    .uri(path)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(CustomerResponse.class)
                    .block();
        } catch (WebClientException e) {
            log.error("Ha ocurrido un error al obtener el cliente por identificación =>", e);
            throw e;
        }
    }
}
