package com.sellosmonterrey.administrador.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.sellosmonterrey.administrador.models.ComputerModel;
import com.sellosmonterrey.administrador.models.UserModel;
import com.sellosmonterrey.administrador.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    // GET

    public ArrayList<UserModel> getUsers(){
        return (ArrayList<UserModel>) userRepository.findAll();
    }
    
    public Optional<UserModel> getUser(long id) {
        return userRepository.findById(id);
    }


    // POST

    public UserModel saveUserModel(UserModel user){
        return userRepository.save(user);
    }

    public List<UserModel> saveUserModels(List<UserModel> users) {
        return (List<UserModel>) userRepository.saveAll(users);
    }


    // DELETE

    public boolean deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        } catch (Exception err) {
            return false;
        }
    }
}
