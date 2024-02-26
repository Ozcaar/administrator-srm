package com.sellosmonterrey.administrador.repositories;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sellosmonterrey.administrador.models.ComputerModel;

@Repository
public interface ComputerRepository extends CrudRepository<ComputerModel, Long> {
    public abstract ArrayList<ComputerModel> findById(Integer computer);
}
