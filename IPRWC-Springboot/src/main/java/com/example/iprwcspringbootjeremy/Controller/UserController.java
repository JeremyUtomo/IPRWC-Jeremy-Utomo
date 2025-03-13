package com.example.iprwcspringbootjeremy.Controller;

import com.example.iprwcspringbootjeremy.DTO.UserDTO;
import com.example.iprwcspringbootjeremy.Model.User;
import com.example.iprwcspringbootjeremy.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(this.userService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<UserDTO> updateUserById(@PathVariable("id") String id, @RequestBody User updatedUser) {
        try {
            return ResponseEntity.ok(this.userService.updateUser(id, updatedUser));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> deleteUserById(@PathVariable("id") String id) {
        try {
            this.userService.deleteUser(id);
            return ResponseEntity.ok("User deleted with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
