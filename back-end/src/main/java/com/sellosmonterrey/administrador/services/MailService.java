package com.sellosmonterrey.administrador.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.sellosmonterrey.administrador.models.MailModel;
import com.sellosmonterrey.administrador.repositories.MailRepository;

@Service
public class MailService {

    @Autowired
    MailRepository mailRepository;

    // GET

    public ArrayList<MailModel> getMails() {
        return (ArrayList<MailModel>) mailRepository.findAll();
    }

    public Optional<MailModel> getMail(long id) {
        return mailRepository.findById(id);
    }

    // POST

    public MailModel saveMailModel(MailModel mail) {
        return mailRepository.save(mail);
    }

    public List<MailModel> saveMailModels(List<MailModel> mails) {
        return (List<MailModel>) mailRepository.saveAll(mails);
    }

    // DELETE

    public boolean deleteMail(Long id) {
        try {
            mailRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        } catch (Exception err) {
            return false;
        }
    }

}
