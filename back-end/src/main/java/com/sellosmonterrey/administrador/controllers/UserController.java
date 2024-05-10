package com.sellosmonterrey.administrador.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import com.sellosmonterrey.administrador.models.UserModel;
import com.sellosmonterrey.administrador.services.AuthService;
import com.sellosmonterrey.administrador.services.UserService;

@RestController
@RequestMapping("/users")
// @CrossOrigin(origins = { "http://localhost:5173", "http://10.21.11.156:5173",
// "http://192.168.1.15:5173" })
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    // GET

    // Get all the users
    @GetMapping
    public ResponseEntity<ArrayList<UserModel>> getUsers(@RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // Get an user by id
    @GetMapping(path = "/{id}")
    public ResponseEntity<Optional<UserModel>> getUser(@PathVariable("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                return ResponseEntity.status(HttpStatus.OK).body(this.userService.getUser(id));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return handleException(e);
        }
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
    public ResponseEntity<UserModel> saveUserModel(@RequestBody UserModel empleado,
            @RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.saveUserModel(empleado));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // Save many users
    @PostMapping("/saveBatch")
    public ResponseEntity<List<UserModel>> saveUsers(@RequestBody List<UserModel> users,
            @RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUserModels(users));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // DELETE

    // Delete an user by id
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id, @RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                if (this.userService.deleteUser(id)) {
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