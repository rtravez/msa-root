package com.rtravez.msa.service.common;

import com.rtravez.msa.config.UrlDependenceWebServices;
import com.rtravez.msa.dto.request.UserRequest;
import com.rtravez.msa.dto.response.UserResponse;
import com.rtravez.msa.util.EnvironmentUtil;
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
    public UserResponse findUserByIdentification(UserRequest request) {
        try {
            String path = EnvironmentUtil.getDomainNameContext(url.getFindUserByIdentification());
            return webClientMcpServices.post()
                    .uri(path)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .block();
        } catch (WebClientException e) {
            log.error("Ha ocurrido un error al obtener el usuario por identificación =>", e);
            throw e;
        }
    }
}
