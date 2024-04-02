package com.sellosmonterrey.administrador.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

    // POST

    public AdminModel savAdminModel(AdminModel admin) {
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

}
