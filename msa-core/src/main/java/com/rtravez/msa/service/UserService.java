package com.rtravez.msa.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rtravez.msa.dto.response.UserResponse;
import com.rtravez.msa.entity.view.UserView;
import com.rtravez.msa.repository.IUserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
@Service
@Slf4j
public class UserService extends GenericService<UserView, Long, IUserRepository> implements IUserService {

    protected UserService(IUserRepository repository) {
        super(repository);
    }

    @Override
    public Optional<UserResponse> findByUsername(String username) {
        return repository.findByUsername(username).map(this::toUserResponse);
    }

    private UserResponse toUserResponse(UserView user) {
        UserResponse response = UserResponse.builder()
            .userId(user.getUserId())
            .username(user.getUsername())
            .build();
        response.setPersonId(user.getPerson().getPersonId());
        response.setIdentification(user.getPerson().getIdentification());
        response.setName(user.getPerson().getName());
        response.setLastname(user.getPerson().getLastname());
        response.setAddress(user.getPerson().getAddress());
        response.setTelephone(user.getPerson().getTelephone());
        response.setGender(user.getPerson().getGender() == null ? null : user.getPerson().getGender().toString());
        response.setAge(user.getPerson().getAge());
        return response;
    }

}
