package com.example.iprwcspringbootjeremy.Repository;

import com.example.iprwcspringbootjeremy.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
