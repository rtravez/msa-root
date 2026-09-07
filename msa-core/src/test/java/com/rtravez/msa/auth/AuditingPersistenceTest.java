package com.rtravez.msa.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.rtravez.msa.entity.view.PersonView;

import jakarta.persistence.EntityManager;

@DataJpaTest 
@Import (AuditingConfig.class)
class AuditingPersistenceTest {

    @Autowired 
    private EntityManager entityManager;

    @AfterEach 
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void fillsAuditFieldsWhenEntityIsPersisted() {
        SecurityContextHolder.getContext().setAuthentication(
                UsernamePasswordAuthenticationToken.authenticated("maria", "N/A", java.util.List.of()));

        PersonView person = PersonView.builder()
                .name("Maria")
                .lastname("Perez")
                .identification("1717172637")
                .build();
        person.setStatus(true);
        person.setCreatedHost("127.0.0.1");

        entityManager.persist(person);
        entityManager.flush();

        assertEquals("maria", person.getCreatedUser());
        assertNotNull(person.getCreatedDate());
        assertNotNull(person.getLastModifiedDate());
        assertEquals("maria", person.getLastModifiedUser());
    }
}

