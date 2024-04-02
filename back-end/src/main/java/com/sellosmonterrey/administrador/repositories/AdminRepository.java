package com.sellosmonterrey.administrador.repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sellosmonterrey.administrador.models.AdminModel;

@Repository
public interface AdminRepository extends CrudRepository<AdminModel, Long> {
    public abstract ArrayList<AdminModel> findById(Integer admin);

    public abstract Optional<AdminModel> findByUsername(String username);
}
