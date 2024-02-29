package com.sellosmonterrey.administrador.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sellosmonterrey.administrador.models.ServiceModel;
import com.sellosmonterrey.administrador.services.ServiceService;

@RestController
@RequestMapping("/services")
@CrossOrigin(origins = { "http://localhost:5173", "http://10.21.11.156:5173", "http://192.168.1.15:5173" })
public class ServiceController {

    @Autowired
    ServiceService serviceService;

    // GET

    // Get all the services
    @GetMapping
    public ResponseEntity<ArrayList<ServiceModel>> getServices() {
        ArrayList<ServiceModel> services = serviceService.getServices();
        return ResponseEntity.ok(services);
    }

    // Geta a service by id
    @GetMapping(path = "/{id}")
    public ServiceModel getService(@PathVariable("id") Long id) {
        return serviceService.getService(id);
    }

    // POST

    // Save a new service
    @PostMapping
    public ResponseEntity<ServiceModel> saveService(@RequestBody ServiceModel service) {
        ServiceModel savedService = serviceService.saveServiceModel(service);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedService);
    }

    // Save many services
    @PostMapping("/saveBatch")
    public ResponseEntity<List<ServiceModel>> saveServices(@RequestBody List<ServiceModel> service) {
        List<ServiceModel> savedServices = serviceService.saveServiceModels(service);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedServices);
    }

    // DELETE

    // Delete a service by id
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteService(@PathVariable("id") Long id) {
        if (serviceService.deleteService(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
