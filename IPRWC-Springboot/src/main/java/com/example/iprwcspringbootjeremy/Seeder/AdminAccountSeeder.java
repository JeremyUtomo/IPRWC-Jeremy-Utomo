package com.example.iprwcspringbootjeremy.Seeder;

import com.example.iprwcspringbootjeremy.DAO.*;
import com.example.iprwcspringbootjeremy.Model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.UUID;

@Component
public class AdminAccountSeeder {

    @Autowired
    private UserDao userDao;

    public void seed(){
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("Jeremy");
        user.setLastName("Utomo");
        user.setEmail("hujeremy@icloud.com");
        user.setPassword("$2a$10$NvMn565mDZjpNsYs6P66fehsLKaeJYFgYDZX5eCAEPVvwnJO6eeBK");
        user.setRole(Role.ADMIN);
        this.userDao.addUser(user);
    }
}
