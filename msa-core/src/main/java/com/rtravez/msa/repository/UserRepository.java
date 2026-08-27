package com.rtravez.msa.repository;

import com.rtravez.msa.entity.view.UserView;
import com.rtravez.msa.exception.ExceptionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.rtravez.msa.entity.view.QPersonView.personView;
import static com.rtravez.msa.entity.view.QUserView.userView;

@Slf4j
@Repository
public class UserRepository extends GenericRepository<UserView, Long> implements IUserRepository {

    /**
     * Constructor
     */
    public UserRepository() {
        super(UserView.class);
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
