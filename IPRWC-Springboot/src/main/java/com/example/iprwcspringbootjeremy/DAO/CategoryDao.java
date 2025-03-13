package com.example.iprwcspringbootjeremy.DAO;

import com.example.iprwcspringbootjeremy.Model.Category;
import com.example.iprwcspringbootjeremy.Repository.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CategoryDao {
    private final CategoryRepository productRepository;

    public CategoryDao(CategoryRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Category getCategoryByID(String id) {
        return productRepository.findById(UUID.fromString(id)).orElse(null);
    }

    public List<Category> getAllCategories() {
        return productRepository.findAll();
    }

    public Category addCategory(Category category) {
        return productRepository.save(category);
    }

    public void deleteCategory(Category category) {
        productRepository.delete(category);
    }
}
