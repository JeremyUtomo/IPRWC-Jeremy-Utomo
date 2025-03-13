package com.example.iprwcspringbootjeremy.Controller;


import com.example.iprwcspringbootjeremy.DTO.CategoryDTO;
import com.example.iprwcspringbootjeremy.Model.Category;
import com.example.iprwcspringbootjeremy.Service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(this.categoryService.getAllCategories());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(this.categoryService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody Category category) {
        return ResponseEntity.ok(this.categoryService.addCategory(category));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable("id") String id, @RequestBody Category updatedCategory) {
        try {
            return ResponseEntity.ok(this.categoryService.updateCategory(id, updatedCategory));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> deleteCategory(@PathVariable("id") String id) {
        try {
            this.categoryService.deleteCategory(id);
            return ResponseEntity.ok("Category deleted with id " + id);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
