package com.example.iprwcspringbootjeremy.Seeder;

import com.example.iprwcspringbootjeremy.DAO.ProductDao;
import com.example.iprwcspringbootjeremy.Model.Category;
import com.example.iprwcspringbootjeremy.Model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ProductSeeder {

    @Autowired
    private ProductDao productDao;

    public void seed(String name, double price, int stock, String description, String imagePath, Category category) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);
        product.setDescription(description);
        try {
            byte[] imageBytes = Files.readAllBytes(Paths.get("/app/images/" + imagePath));
            product.setImage(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            product.setId(null);
        }
        product.setCategory(category);

        this.productDao.addProduct(product);
    }
}
