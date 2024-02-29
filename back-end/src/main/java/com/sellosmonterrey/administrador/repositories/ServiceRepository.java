package com.sellosmonterrey.administrador.repositories;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sellosmonterrey.administrador.models.ServiceModel;

@Repository
public interface ServiceRepository extends CrudRepository<ServiceModel, Long> {
    public abstract ArrayList<ServiceModel> findById(Integer service);
}
