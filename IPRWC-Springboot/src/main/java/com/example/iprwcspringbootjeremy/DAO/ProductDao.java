package com.example.iprwcspringbootjeremy.DAO;

import com.example.iprwcspringbootjeremy.Model.Product;
import com.example.iprwcspringbootjeremy.Repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ProductDao {
    private final ProductRepository productRepository;

    public ProductDao(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductByID(String id) {
        return productRepository.findById(UUID.fromString(id)).orElse(null);
    }

    public Product getProductByID(UUID id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product getProductByName(String name) {
        return productRepository.findByName(name);
    }

    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    public void updateStock(UUID productId, int i) {

    }
}