package com.rtravez.msa.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.rtravez.msa.dto.BaseResponseDto;
import com.rtravez.msa.dto.response.UserResponse;
import com.rtravez.msa.entity.view.UserView;
import com.rtravez.msa.exception.ExceptionManager;
import com.rtravez.msa.mapper.UserMapper;
import com.rtravez.msa.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RestClient mscServices;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<UserResponse> findByUsername(String username) {
        return userRepository.findByUsername(username).map(userMapper::toResponse);
    }    

    @Override
    public UserResponse findUserByIdentification(String identification) throws ExceptionManager {
        try {
            ResponseEntity<BaseResponseDto<UserResponse>> response = mscServices.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/users")
                            .queryParam("identification", identification)
                            .build())
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<>() {
                    });
            BaseResponseDto<UserResponse> body = response.getBody();
            return body != null ? body.getData() : null;
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
