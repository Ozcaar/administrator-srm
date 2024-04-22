package com.sellosmonterrey.administrador.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sellosmonterrey.administrador.services.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = { "http://localhost:5173", "http://10.21.11.156:5173", "http://192.168.1.15:5173" })
public class AuthController {

    @Autowired
    AuthService authService;

    @GetMapping
    public ResponseEntity<String> login(@RequestParam("username") String username,
            @RequestParam("password") String password) {

        try {
            String token = authService.authenticate(username, password);
            return ResponseEntity.ok(token);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping(path = "/current")
    public ResponseEntity<String> currentSession(@RequestParam("username") String username) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(authService.getCurrentToken(username));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
