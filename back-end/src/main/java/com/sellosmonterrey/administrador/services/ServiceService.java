package com.sellosmonterrey.administrador.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.sellosmonterrey.administrador.models.ServiceModel;
import com.sellosmonterrey.administrador.repositories.ServiceRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ServiceService {

    @Autowired
    ServiceRepository serviceRepository;

    // GET

    public ArrayList<ServiceModel> getServices() {
        return (ArrayList<ServiceModel>) serviceRepository.findAll();
    }

    public ServiceModel getService(long id) {
        return serviceRepository.findById(id).orElse(null);
    }

    // POST

    public ServiceModel saveServiceModel(ServiceModel service) {
        return serviceRepository.save(service);
    }

    public List<ServiceModel> saveServiceModels(List<ServiceModel> services) {
        return (List<ServiceModel>) serviceRepository.saveAll(services);
    }

    // DELETE

    public boolean deleteService(long id) {
        try {
            serviceRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException err) {
            return false;
        } catch (Exception err) {
            return false;
        }
    }

}
