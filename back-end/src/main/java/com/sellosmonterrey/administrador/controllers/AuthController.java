package com.sellosmonterrey.administrador.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sellosmonterrey.administrador.models.AdminModel;
import com.sellosmonterrey.administrador.repositories.AdminRepository;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = { "http://localhost:5173", "http://10.21.11.156:5173", "http://192.168.1.15:5173" })
public class AuthController {

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping
    public ResponseEntity<String> login(@RequestParam("username") String username,
            @RequestParam("password") String password) {

        // Find by username
        Optional<AdminModel> adminOptional = adminRepository.findByUsername(username);

        if (adminOptional.isPresent()) {
            AdminModel admin = adminOptional.get();

            // Verify password
            if (verifyPassword(password, admin.getPasswordHashed())) {
                String token = generateToken(); // Generate random token
                admin.setToken(token); // Update the token in the admin's table of db
                adminRepository.save(admin);
                return ResponseEntity.ok(token); // Return token as response
            }
        }
        // If invalid return unauthorized
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private boolean verifyPassword(String rawPassword, String hashedPassword) {
        // Verfy if password provided matches with the hash in the db
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    private String generateToken() {
        // Generate a session token
        return UUID.randomUUID().toString();
    }

}
