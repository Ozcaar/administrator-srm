package com.sellosmonterrey.administrador.controllers;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sellosmonterrey.administrador.models.ComputerModel;
import com.sellosmonterrey.administrador.services.ComputerService;

@RestController
@RequestMapping("/computers")
@CrossOrigin(origins = "http://localhost:5173")
public class ComputerController {
    
    @Autowired
    ComputerService computerService;

    // GET

    // Get all the computers
    @GetMapping
    public ResponseEntity<ArrayList<ComputerModel>> getComputers() {
        ArrayList<ComputerModel> computers = computerService.getComputers();
        return ResponseEntity.ok(computers);
    }

    // Get a computer by id
    @GetMapping(path = "/{id}")
    public ComputerModel getComputer(@PathVariable("id") Long id) {
        return computerService.getComputer(id);
    }


    // POST

    // Save a new computer
    @PostMapping
    public ResponseEntity<ComputerModel> saveComputer(@RequestBody ComputerModel computer) {
        ComputerModel savedComputer = computerService.saveComputerModel(computer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComputer);
    }

    // Save many computers
    @PostMapping("/saveBatch")
    public ResponseEntity<List<ComputerModel>> saveComputers(@RequestBody List<ComputerModel> computers) {
        List<ComputerModel> savedComputers = computerService.saveComputerModels(computers);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComputers);
    }


    // DELETE

    // Delete a computer by id
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteComputer(@PathVariable("id") Long id) {
        if (computerService.deleteComputer(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
