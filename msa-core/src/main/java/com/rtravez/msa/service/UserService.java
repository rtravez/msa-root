package com.rtravez.msa.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

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
    public Optional<UserView> findByUsername(String username) {
        return repository.findByUsername(username);
    }

}
