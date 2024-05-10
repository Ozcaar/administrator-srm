package com.sellosmonterrey.administrador.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import com.sellosmonterrey.administrador.models.ServiceModel;
import com.sellosmonterrey.administrador.services.AuthService;
import com.sellosmonterrey.administrador.services.ServiceService;

@RestController
@RequestMapping("/services")
// @CrossOrigin(origins = { "http://localhost:5173", "http://10.21.11.156:5173",
// "http://192.168.1.15:5173" })
public class ServiceController {

    @Autowired
    ServiceService serviceService;

    @Autowired
    AuthService authService;

    // GET

    // Get all the services
    @GetMapping
    public ResponseEntity<ArrayList<ServiceModel>> getServices(@RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                return ResponseEntity.status(HttpStatus.OK).body(serviceService.getServices());
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // Geta a service by id
    @GetMapping(path = "/{id}")
    public ResponseEntity<ServiceModel> getService(@PathVariable("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                return ResponseEntity.status(HttpStatus.OK).body(serviceService.getService(id));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // POST

    // Save a new service
    @PostMapping
    public ResponseEntity<ServiceModel> saveService(@RequestBody ServiceModel service,
            @RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                return ResponseEntity.status(HttpStatus.OK).body(serviceService.saveServiceModel(service));
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // Save many services
    @PostMapping("/saveBatch")
    public ResponseEntity<List<ServiceModel>> saveServices(@RequestBody List<ServiceModel> service,
            @RequestParam("username") String username,
            @RequestParam("token") String token) {

        try {
            if (authService.authCurrentSession(username, token)) {
                return ResponseEntity.status(HttpStatus.CREATED).body(serviceService.saveServiceModels(service));
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // DELETE

    // Delete a service by id
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteService(@PathVariable("id") Long id, @RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                if (this.serviceService.deleteService(id)) {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // HANDLE EXCEPTIONS

    private <T> ResponseEntity<T> handleException(Exception e) {
        if (e instanceof AccessDeniedException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } else if (e instanceof NoSuchElementException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
