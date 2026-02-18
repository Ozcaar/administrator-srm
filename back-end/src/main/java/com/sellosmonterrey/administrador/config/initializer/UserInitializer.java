package com.sellosmonterrey.administrador.config.initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sellosmonterrey.administrador.models.AdminModel;
import com.sellosmonterrey.administrador.repositories.AdminRepository;
import com.sellosmonterrey.administrador.services.AdminService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final AdminService adminService;

    @Override
    public void run(String... args) throws Exception {
        createAdminIfNotExists("admin", "adminpassword");
    }

    private void createAdminIfNotExists(String adminName, String password) {
        if (!adminRepository.findByUsername(adminName).isPresent()) {
            AdminModel admin = new AdminModel();
            admin.setUsername(adminName);
            admin.setPassword(password);

            // adminRepository.save(admin);
            admin = adminService.saveAdminModel(admin);
            System.out.println("Usuario administrador creado: " + adminName);
        }
    }
}