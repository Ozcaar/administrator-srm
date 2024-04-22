package com.sellosmonterrey.administrador.services;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sellosmonterrey.administrador.models.AdminModel;

@Service
public class AuthService {

    @Autowired
    private AdminService adminService;

    // GET CURRETN TOKEN

    public String getCurrentToken(String username) {
        AdminModel admin = adminService.getAdminByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        return admin.getToken();
    }

    // COMPARE CLIENT TOKEN WiTH DB TOKEN

    public Boolean authCurrentSession(String username, String token) {
        AdminModel admin = adminService.getAdminByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!admin.getToken().equals(token)) {
            throw new AccessDeniedException("Bad token");
        }
        return true;
    }

    // COMPARE PASSWORDS AND GENERATE TOKEN

    public String authenticate(String username, String password) {

        AdminModel admin = adminService.getAdminByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (verifyPassword(password, admin.getPassword(), admin.getSalt())) {
            String token = generateToken();
            admin.setToken(token);

            // Set last login
            admin.setLast_login(LocalDate.now());

            adminService.saveAdminModel(admin);
            return token;
        } else {
            throw new AccessDeniedException("Invalid password");
        }
    }

    private boolean verifyPassword(String rawPassword, String encodedPassword, String salt) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(salt + rawPassword, encodedPassword);
    }

    public String generateToken() {
        // Generate a session token
        return UUID.randomUUID().toString();
    }
}
