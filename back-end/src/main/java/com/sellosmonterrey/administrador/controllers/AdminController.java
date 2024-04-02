package com.sellosmonterrey.administrador.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sellosmonterrey.administrador.models.AdminModel;
import com.sellosmonterrey.administrador.services.AdminService;

@RestController
@RequestMapping("/admins")
@CrossOrigin(origins = { "http://localhost:5173", "http://10.21.11.156:5173", "http://192.168.1.15:5173" })
public class AdminController {

    @Autowired
    AdminService adminService;

    // GET

    // Get all the users
    public ArrayList<AdminModel> getAdmins() {
        return adminService.getAdmins();
    }

    // Get an user by id
    @GetMapping(path = "/{id}")
    public Optional<AdminModel> getAdmin(@PathVariable("id") Long id) {
        return this.adminService.getAdmin(id);
    }

    // POST

    // Save a new admin
    @PostMapping
    public AdminModel saveAdminModel(@RequestBody AdminModel admin) {
        return this.adminService.savAdminModel(admin);
    }

    // Save many admins
    @PostMapping("/saveBatch")
    public ResponseEntity<List<AdminModel>> saveAdmins(@RequestBody List<AdminModel> admins) {
        List<AdminModel> savedAdmins = adminService.saveAdminModels(admins);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmins);
    }

    // DELETE

    // Delete an admin by id
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteAdmin(@PathVariable("id") Long id) {
        boolean ok = this.adminService.deleteAdmin(id);
        if (ok) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
