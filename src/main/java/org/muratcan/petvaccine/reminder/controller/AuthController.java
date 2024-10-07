package org.muratcan.petvaccine.reminder.controller;

import lombok.RequiredArgsConstructor;
import org.muratcan.petvaccine.reminder.dto.LoginRequestDTO;
import org.muratcan.petvaccine.reminder.dto.UserRegistrationDTO;
import org.muratcan.petvaccine.reminder.service.KeycloakService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakService keycloakService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        ResponseEntity<String> response = keycloakService.registerUser(userRegistrationDTO);

        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("User registered successfully.");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("User registration failed." + response.getBody());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        ResponseEntity<Map<String, Object>> response = keycloakService.login(loginRequestDTO);

        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }
    }
}
