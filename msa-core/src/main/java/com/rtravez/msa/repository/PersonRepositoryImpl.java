package com.rtravez.msa.repository;

import com.rtravez.msa.entity.view.PersonView;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class PersonRepositoryImpl extends BaseRepositoryImpl<PersonView, Long> implements PersonRepository {
    /**
     * Constructor
     */
    public PersonRepositoryImpl(EntityManager em) {
        super(PersonView.class, em);
    }
}
