package com.sellosmonterrey.administrador.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sellosmonterrey.administrador.models.UserModel;
import com.sellosmonterrey.administrador.services.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"http://localhost:5173", "http://10.21.11.156:5173"})
public class UserController {
    @Autowired
    UserService userService;

    // GET

    // Get all the users
    @GetMapping
    public ArrayList<UserModel> getUsers() {
        return userService.getUsers();
    }

    // Get an user by id
    @GetMapping(path = "/{id}")
    public Optional<UserModel> getUser(@PathVariable("id") Long id) {
        return this.userService.getUser(id);
    }

    // Get an user by another param
    // @GetMapping("/query")
    // public ArrayList<UserModel>
    // obtenerUsiarioPorDepartamento(@RequestParam("departamento") Integer
    // departamento) {
    // return this.userService.obtenerPorDepartamento(departamento);
    // }

    // POST

    // Save a new user
    @PostMapping
    public UserModel saveUserModel(@RequestBody UserModel empleado) {
        return this.userService.saveUserModel(empleado);
    }

    // Save many users
    @PostMapping("/saveBatch")
    public ResponseEntity<List<UserModel>> saveUsers(@RequestBody List<UserModel> users) {
        List<UserModel> savedUsers = userService.saveUserModels(users);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUsers);
    }

    // DELETE

    // Delete an user by id
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id) {
        boolean ok = this.userService.deleteUser(id);
        if (ok) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}