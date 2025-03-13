package com.example.iprwcspringbootjeremy.DAO;

import com.example.iprwcspringbootjeremy.Model.*;
import com.example.iprwcspringbootjeremy.Repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.List;
import java.util.UUID;

@Component
public class UserDao {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User addUser(User newUser) {
        return this.userRepository.save(newUser);
    }

    public User getById(String id) {
        return this.userRepository.findById(UUID.fromString(id)).orElse(null);
    }

    public User getById(UUID id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public void deleteUser(User userToDelete) {
        this.userRepository.delete(userToDelete);
    }
}
