package com.sellosmonterrey.administrador.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.sellosmonterrey.administrador.models.ComputerModel;
import com.sellosmonterrey.administrador.repositories.ComputerRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComputerService {
    
    @Autowired
    ComputerRepository computerRepository;

    // GET

    public ArrayList<ComputerModel> getComputers() {
        return (ArrayList<ComputerModel>) computerRepository.findAll();
    }

    public ComputerModel getComputer(long id) {
        return computerRepository.findById(id).orElse(null);
    }


    //POST

    public ComputerModel saveComputerModel(ComputerModel computer) {
        return computerRepository.save(computer);
    }

    public List<ComputerModel> saveComputerModels(List<ComputerModel> computers) {
        return (List<ComputerModel>) computerRepository.saveAll(computers);
    }


    // DELETE

    public boolean deleteComputer(long id) {
        try {
            computerRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException err) {
            return false;
        } catch (Exception err) {
            return false;
        }
    }

}
