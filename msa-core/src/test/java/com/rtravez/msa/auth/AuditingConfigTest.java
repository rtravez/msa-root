package com.rtravez.msa.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

class AuditingConfigTest {

    private final AuditingConfig auditingConfig = new AuditingConfig();

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void returnsAuthenticatedPrincipalName() {
        SecurityContextHolder.getContext().setAuthentication(
                UsernamePasswordAuthenticationToken.authenticated("maria", "N/A", java.util.List.of()));

        assertEquals("maria", auditingConfig.auditorAware().getCurrentAuditor().orElseThrow());
    }

    @Test
    void returnsSystemWhenThereIsNoAuthenticatedUser() {
        assertEquals("system", auditingConfig.auditorAware().getCurrentAuditor().orElseThrow());
    }
}
