package com.rtravez.msa.service.common;

import static java.util.Objects.requireNonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import com.rtravez.msa.config.UrlDependenceWebServices;
import com.rtravez.msa.dto.request.UserRequest;
import com.rtravez.msa.dto.response.UserResponse;
import com.rtravez.msa.util.EnvironmentUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

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
                    .uri(requireNonNull(path, "path must not be null"))
                    .bodyValue(requireNonNull(request, "request must not be null"))
                    .retrieve()
                    .onStatus(status -> status.value() == 404, response -> Mono.empty())
                    .bodyToMono(UserResponse.class)
                    .block();
        } catch (WebClientRequestException e) {
            log.error("No fue posible conectar con el servicio de usuarios", e);
            return null;
        } catch (WebClientException e) {
            log.error("Ha ocurrido un error al obtener el usuario por identificación", e);
            return null;
        }
    }
}
