package com.sellosmonterrey.administrador.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sellosmonterrey.administrador.models.AdminModel;
import com.sellosmonterrey.administrador.repositories.AdminRepository;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    // GET

    public ArrayList<AdminModel> getAdmins() {
        return (ArrayList<AdminModel>) adminRepository.findAll();
    }

    public Optional<AdminModel> getAdmin(long id) {
        return adminRepository.findById(id);
    }

    public Optional<AdminModel> getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    // POST

    public AdminModel saveAdminModel(AdminModel admin) {

        // Generate the salt
        int saltLength = 16;
        String salt = generateSalt(saltLength);

        // Get password from AdminModel instance
        String password = admin.getPassword();

        // Combine salt and password
        String saltedPassword = salt + password;

        // Prepare encoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Encode password
        String hashedPassword = passwordEncoder.encode(saltedPassword);

        // Set the salt and password hashed back to the AdminModel instance
        admin.setSalt(salt);
        admin.setPassword(hashedPassword);

        // Save AdminModel instance
        return adminRepository.save(admin);
    }

    public List<AdminModel> saveAdminModels(List<AdminModel> admins) {
        return (List<AdminModel>) adminRepository.saveAll(admins);
    }

    // DELETE

    public boolean deleteAdmin(Long id) {
        try {
            adminRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        } catch (Exception err) {
            return false;
        }
    }

    // ENCODE PASSWORD

    public String encodePassword(String rawPassword) {

        // Generate the salt
        int saltLength = 16;
        String salt = generateSalt(saltLength);

        // Get password from AdminModel instance
        String password = rawPassword;

        // Combine salt and password
        String saltedPassword = salt + password;

        // Prepare encoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Encode password
        String passwordHashed = passwordEncoder.encode(saltedPassword);

        return passwordHashed;
    }

    // SALT GENERATOR

    public static String generateSalt(int length) {
        try {
            byte[] salt = new byte[length];
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.nextBytes(salt);
            return Base64.getEncoder().encodeToString(salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
