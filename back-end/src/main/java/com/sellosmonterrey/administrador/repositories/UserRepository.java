package com.sellosmonterrey.administrador.repositories;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sellosmonterrey.administrador.models.UserModel;

@Repository
public interface UserRepository extends CrudRepository<UserModel, Long> {
    public abstract ArrayList<UserModel> findById(Integer app_user);
}
