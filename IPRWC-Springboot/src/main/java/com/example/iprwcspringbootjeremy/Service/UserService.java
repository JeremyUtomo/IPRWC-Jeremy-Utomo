package com.example.iprwcspringbootjeremy.Service;

import com.example.iprwcspringbootjeremy.DAO.UserDao;
import com.example.iprwcspringbootjeremy.DTO.UserDTO;
import com.example.iprwcspringbootjeremy.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = this.userDao.getAllUsers();
        List<UserDTO> userDTOS = new ArrayList<>(users.size());
        for (User user : users) {
            userDTOS.add(user.toDTO());
        }
        return userDTOS;
    }

    public UserDTO getById(String id) {
        User user = this.userDao.getById(id);
        return user.toDTO();
    }

    public UserDTO updateUser(String id, User updatedUser) {
        User user = this.userDao.getById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setRole(updatedUser.getRole());
        User updated = this.userDao.addUser(user);
        return updated.toDTO();
    }

    public void deleteUser(String id) {
        User userToDelete = this.userDao.getById(id);
        if (userToDelete == null) {
            throw new IllegalArgumentException("User not found");
        }
        this.userDao.deleteUser(userToDelete);
    }
}
