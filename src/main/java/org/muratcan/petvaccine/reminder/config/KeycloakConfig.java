package org.muratcan.petvaccine.reminder.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class KeycloakConfig {
    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;
    @Value("${keycloak-service.params.realms-url}")
    private String realmUrl;
    @Value("${keycloak-service.params.admin-url}")
    private String adminUrl;
    @Value("${keycloak.realm-key}")
    private String realmKey;
}
