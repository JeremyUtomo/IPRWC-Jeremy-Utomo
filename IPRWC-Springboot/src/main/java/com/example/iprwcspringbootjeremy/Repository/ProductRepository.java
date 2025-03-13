package com.example.iprwcspringbootjeremy.Repository;

import com.example.iprwcspringbootjeremy.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Product findByName(String name);
}