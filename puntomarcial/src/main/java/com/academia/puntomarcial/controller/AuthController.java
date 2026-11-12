package com.academia.puntomarcial.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academia.puntomarcial.dto.LoginRequest;
import com.academia.puntomarcial.dto.LoginResponse;
import com.academia.puntomarcial.service.AuthService;

@RestController 
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse respuesta = authService.login(request.getCorreo(), request.getPassword());
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}