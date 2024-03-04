package com.sellosmonterrey.administrador.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sellosmonterrey.administrador.models.MailModel;
import com.sellosmonterrey.administrador.services.MailService;

@RestController
@RequestMapping("/mails")
@CrossOrigin(origins = { "http://localhost:5173", "http://10.21.11.156:5173", "http://192.168.1.15:5173" })
public class MailController {

    @Autowired
    MailService mailService;

    // GET

    // Get all the mails
    @GetMapping
    public ArrayList<MailModel> getMails() {
        return mailService.getMails();
    }

    // Get a mail by id
    @GetMapping(path = "/{id}")
    public Optional<MailModel> getMail(@PathVariable("id") Long id) {
        return this.mailService.getMail(id);
    }

    // POST

    // Save a new mail
    @PostMapping
    public MailModel savMailModel(@RequestBody MailModel mail) {
        return this.mailService.saveMailModel(mail);
    }

    // Save many mails
    @PostMapping("/saveBatch")
    public ResponseEntity<List<MailModel>> saveMails(@RequestBody List<MailModel> mails) {
        List<MailModel> savedMails = this.mailService.saveMailModels(mails);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMails);
    }

    // DELETE

    // Delete a mail by id
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteMail(@PathVariable("id") Long id) {
        boolean ok = this.mailService.deleteUser(id);
        if (ok) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
