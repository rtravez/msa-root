package com.rtravez.msa.repository;

import static com.rtravez.msa.entity.view.QPersonView.personView;
import static com.rtravez.msa.entity.view.QUserView.userView;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.rtravez.msa.entity.view.UserView;
import com.rtravez.msa.exception.ExceptionManager;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<UserView, Long> implements UserRepository {

    /**
     * Constructor
     */
    public UserRepositoryImpl(EntityManager em) {
        super(UserView.class,em);
    }

    @Override
    public Optional<UserView> findByUsername(String username) throws ExceptionManager {
        try {
            return Optional.ofNullable(queryFactory.selectFrom(userView).innerJoin(userView.person, personView)
                    .fetchJoin().where(userView.username.eq(username).and(userView.status.isTrue()))
                    .fetchFirst());
        } catch (Exception e) {
            log.error("findByUsername: ", e);
            throw new ExceptionManager.FindingException("Error al buscar el registro");
        }
    }

}
