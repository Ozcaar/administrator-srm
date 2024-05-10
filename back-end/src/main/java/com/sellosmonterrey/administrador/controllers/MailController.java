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

import com.sellosmonterrey.administrador.models.MailModel;
import com.sellosmonterrey.administrador.services.AuthService;
import com.sellosmonterrey.administrador.services.MailService;

@RestController
@RequestMapping("/mails")
// @CrossOrigin(origins = { "http://localhost:5173", "http://10.21.11.156:5173",
// "http://192.168.1.15:5173" })
public class MailController {

    @Autowired
    MailService mailService;

    @Autowired
    AuthService authService;

    // GET

    // Get all the mails
    @GetMapping
    public ResponseEntity<ArrayList<MailModel>> getMails(@RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                return ResponseEntity.status(HttpStatus.OK).body(mailService.getMails());
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // Get a mail by id
    @GetMapping(path = "/{id}")
    public ResponseEntity<Optional<MailModel>> getMail(@PathVariable("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                return ResponseEntity.status(HttpStatus.OK).body(this.mailService.getMail(id));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // POST

    // Save a new mail
    @PostMapping
    public ResponseEntity<MailModel> savMailModel(@RequestBody MailModel mail,
            @RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                return ResponseEntity.status(HttpStatus.CREATED).body(this.mailService.saveMailModel(mail));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // Save many mails
    @PostMapping("/saveBatch")
    public ResponseEntity<List<MailModel>> saveMails(@RequestBody List<MailModel> mails,
            @RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                return ResponseEntity.status(HttpStatus.CREATED).body(this.mailService.saveMailModels(mails));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // DELETE

    // Delete a mail by id
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteMail(@PathVariable("id") Long id, @RequestParam("username") String username,
            @RequestParam("token") String token) {
        try {
            if (authService.authCurrentSession(username, token)) {
                if (this.mailService.deleteMail(id)) {
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
