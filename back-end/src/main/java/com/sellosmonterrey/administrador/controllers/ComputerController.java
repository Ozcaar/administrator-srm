package com.sellosmonterrey.administrador.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import com.sellosmonterrey.administrador.models.ComputerModel;
import com.sellosmonterrey.administrador.services.AuthService;
import com.sellosmonterrey.administrador.services.ComputerService;

@RestController
@RequestMapping("/computers")
// @CrossOrigin(origins = { "http://localhost:5173", "http://10.21.11.156:5173",
// "http://192.168.1.15:5173" })
public class ComputerController {

    @Autowired
    ComputerService computerService;

    @Autowired
    AuthService authService;

    // GET

    // Get all the computers
    @GetMapping
    public ResponseEntity<ArrayList<ComputerModel>> getComputers(@RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                return ResponseEntity.status(HttpStatus.OK).body(computerService.getComputers());
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // Get a computer by id
    @GetMapping(path = "/{id}")
    public ResponseEntity<ComputerModel> getComputer(@PathVariable("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                return ResponseEntity.status(HttpStatus.OK).body(computerService.getComputer(id));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // POST

    // Save a new computer
    @PostMapping
    public ResponseEntity<ComputerModel> saveComputer(@RequestBody ComputerModel computer,
            @RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                return ResponseEntity.status(HttpStatus.CREATED).body(computerService.saveComputerModel(computer));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // Save many computers
    @PostMapping("/saveBatch")
    public ResponseEntity<List<ComputerModel>> saveComputers(@RequestBody List<ComputerModel> computers,
            @RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                return ResponseEntity.status(HttpStatus.CREATED).body(computerService.saveComputerModels(computers));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // DELETE

    // Delete a computer by id
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteComputer(@PathVariable("id") Long id, @RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                if (this.computerService.deleteComputer(id)) {
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
