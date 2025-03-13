package com.example.iprwcspringbootjeremy.Seeder;

import com.example.iprwcspringbootjeremy.DAO.CategoryDao;
import com.example.iprwcspringbootjeremy.Model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CategorySeeder {

    @Autowired
    private CategoryDao categoryDao;

    public Category seed(String name) {
        Category category = new Category();
        category.setName(name);

        this.categoryDao.addCategory(category);
        return category;
    }
}
