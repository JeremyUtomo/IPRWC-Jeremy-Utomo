package com.example.iprwcspringbootjeremy.Service;

import com.example.iprwcspringbootjeremy.DAO.CategoryDao;
import com.example.iprwcspringbootjeremy.DTO.CategoryDTO;
import com.example.iprwcspringbootjeremy.Model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryDao categoryDao;

    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryDao.getAllCategories();
        List<CategoryDTO> categoryDTOS = new ArrayList<>(categories.size());
        for (Category category : categories) {
            categoryDTOS.add(category.toDTO());
        }
        return categoryDTOS;
    }

    public CategoryDTO getById(String id) {
        Category category = categoryDao.getCategoryByID(id);
        return category.toDTO();
    }

    public CategoryDTO addCategory(Category category) {
        Category addedCategory = categoryDao.addCategory(category);
        return addedCategory.toDTO();
    }

    public CategoryDTO updateCategory(String id, Category updatedCategory) {
        Category category = categoryDao.getCategoryByID(id);
        if (category == null) {
            throw new IllegalArgumentException("Category not found");
        }
        category.setName(updatedCategory.getName());
        Category updated = categoryDao.addCategory(category);
        return updated.toDTO();
    }

    public void deleteCategory(String id) {
        Category category = categoryDao.getCategoryByID(id);
        if (category == null) {
            throw new IllegalArgumentException("Category not found");
        }
        categoryDao.deleteCategory(category);
    }
}
