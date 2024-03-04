package com.sellosmonterrey.administrador.repositories;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sellosmonterrey.administrador.models.MailModel;

@Repository
public interface MailRepository extends CrudRepository<MailModel, Long> {
    public abstract ArrayList<MailModel> findById(Integer mail);
}
