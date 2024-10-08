package org.muratcan.petvaccine.reminder.service;

import lombok.RequiredArgsConstructor;
import org.muratcan.petvaccine.reminder.config.KeycloakConfig;
import org.muratcan.petvaccine.reminder.constant.KeycloakConstants;
import org.muratcan.petvaccine.reminder.dto.LoginRequestDTO;
import org.muratcan.petvaccine.reminder.dto.UserRegistrationDTO;
import org.muratcan.petvaccine.reminder.entity.User;
import org.muratcan.petvaccine.reminder.exception.KeycloakException;
import org.muratcan.petvaccine.reminder.util.JwtUtil;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final JwtUtil jwtUtil;

    private final KeycloakConfig keycloakConfig;

    private final UserService userService;

    public ResponseEntity<Map<String, Object>> login(LoginRequestDTO loginRequestDTO) {

        final String tokenUrl = keycloakConfig.getRealmUrl() + keycloakConfig.getRealm() + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = createLoginRequestEntity(loginRequestDTO, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {
                }
        );

        return ResponseEntity.ok(response.getBody());
    }

    public ResponseEntity<String> registerUser(UserRegistrationDTO userRegistrationDTO) {
        String registrationUrl = String.format("%s/%s/users", keycloakConfig.getAdminUrl(), keycloakConfig.getRealm());

        String adminAccessToken = getAdminAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminAccessToken);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(KeycloakConstants.USERNAME, userRegistrationDTO.getUsername());
        requestBody.put(KeycloakConstants.EMAIL, userRegistrationDTO.getEmail());
        requestBody.put(KeycloakConstants.FIRST_NAME, userRegistrationDTO.getFirstName());
        requestBody.put(KeycloakConstants.LAST_NAME, userRegistrationDTO.getLastName());
        requestBody.put(KeycloakConstants.ENABLED, true);

        Map<String, String> credentials = new HashMap<>();
        credentials.put(KeycloakConstants.TYPE, KeycloakConstants.PASSWORD);
        credentials.put(KeycloakConstants.VALUE, userRegistrationDTO.getPassword());
        requestBody.put(KeycloakConstants.CREDENTIALS, List.of(credentials));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(registrationUrl, entity, String.class);

            LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
            loginRequestDTO.setUsername(userRegistrationDTO.getUsername());
            loginRequestDTO.setPassword(userRegistrationDTO.getPassword());
            ResponseEntity<Map<String, Object>> loginResponse = login(loginRequestDTO);

            User user = new User();
            user.setUsername(userRegistrationDTO.getUsername());
            user.setEmail(userRegistrationDTO.getEmail());
            user.setId(jwtUtil.getUserId(Objects.requireNonNull(loginResponse.getBody()).get(KeycloakConstants.ACCESS_TOKEN).toString()));

            userService.saveUser(user);
            return response;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                return handleConflictError(e.getResponseBodyAsString());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("An error occurred while registering the user.");
            }
        }
    }

    private ResponseEntity<String> handleConflictError(String errorMessage) {
        String message;
        if (errorMessage.contains("User exists with same email")) {
            message = "Email is already in use. Please choose a different email.";
        } else if (errorMessage.contains("User exists with same username")) {
            message = "Username is already taken. Please choose a different username.";
        } else {
            message = "User registration conflict: " + errorMessage;
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    private String getAdminAccessToken() {
        final String tokenUrl = keycloakConfig.getRealmUrl() + keycloakConfig.getRealm() + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add(KeycloakConstants.GRANT_TYPE, KeycloakConstants.CLIENT_CREDENTIALS);
        requestBody.add(KeycloakConstants.CLIENT_ID, keycloakConfig.getClientId());
        requestBody.add(KeycloakConstants.CLIENT_SECRET, keycloakConfig.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {
                }
        );

        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey(KeycloakConstants.ACCESS_TOKEN)) {
            return responseBody.get(KeycloakConstants.ACCESS_TOKEN).toString();
        }
        throw new KeycloakException("Failed to retrieve admin access token from Keycloak");
    }

    private HttpEntity<MultiValueMap<String, String>> createLoginRequestEntity(LoginRequestDTO loginRequestDTO, HttpHeaders headers) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add(KeycloakConstants.GRANT_TYPE, KeycloakConstants.PASSWORD);
        requestBody.add(KeycloakConstants.CLIENT_ID, keycloakConfig.getClientId());
        requestBody.add(KeycloakConstants.CLIENT_SECRET, keycloakConfig.getClientSecret());
        requestBody.add(KeycloakConstants.USERNAME, loginRequestDTO.getUsername());
        requestBody.add(KeycloakConstants.PASSWORD, loginRequestDTO.getPassword());

        return new HttpEntity<>(requestBody, headers);
    }
}

