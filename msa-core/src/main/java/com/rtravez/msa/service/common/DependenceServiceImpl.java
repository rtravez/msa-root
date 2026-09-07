package com.rtravez.msa.service.common;

import static java.util.Objects.requireNonNull;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.rtravez.msa.config.UrlDependenceWebServices;
import com.rtravez.msa.dto.request.UserRequest;
import com.rtravez.msa.dto.response.UserResponse;
import com.rtravez.msa.exception.ExceptionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@RequiredArgsConstructor 
public class DependenceServiceImpl implements DependenceService {

    private final UrlDependenceWebServices url;
    private final @Qualifier("restClientMcpServices") RestClient restClientMcpServices;

    @Override
    public UserResponse findUserByIdentification(UserRequest request) {
        try {
            String path = url.getFindUserByIdentification();
            return restClientMcpServices.post()
                    .uri(requireNonNull(path, "path must not be null"))
                    .body(requireNonNull(request, "request must not be null"))
                    .retrieve()
                    .body(UserResponse.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (ResourceAccessException e) {
            log.error("No fue posible conectar con el servicio de usuarios", e);
            throw new ExceptionManager.ServiceUnavailableException("El servicio de usuarios no está disponible");
        } catch (RestClientException e) {
            log.error("Ha ocurrido un error al obtener el usuario por identificación", e);
            throw new ExceptionManager.ServiceUnavailableException("El servicio de usuarios no está disponible");
        }
    }
}
