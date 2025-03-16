package com.example.iprwcspringbootjeremy.Service;

import com.example.iprwcspringbootjeremy.DAO.CategoryDao;
import com.example.iprwcspringbootjeremy.DAO.ProductDao;
import com.example.iprwcspringbootjeremy.DTO.Request.ProductRequest;
import com.example.iprwcspringbootjeremy.DTO.Response.ProductResponse;
import com.example.iprwcspringbootjeremy.Model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ProductService {

    private final ProductDao productDao;
    private final CategoryDao categoryDao;
    @Value("${IMAGE_PATH}")
    private String IMAGE_PATH;

    public ProductService(ProductDao productDao, CategoryDao categoryDao) {
        this.productDao = productDao;
        this.categoryDao = categoryDao;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = this.productDao.getAllProducts();
        List<ProductResponse> productResponses = new ArrayList<>(products.size());
        for (Product product : products) {
            productResponses.add(product.toDTO());
        }
        return productResponses;
    }

    public ProductResponse getById(String id) {
        return this.productDao.getProductByID(id).toDTO();
    }

    public ProductResponse getByName(String name) {
        Product product = this.productDao.getProductByName(name);
        return product.toDTO();
    }

    public ProductResponse addProduct(ProductRequest productRequest) {
        Product product = new Product();
        return this.productDao.addProduct(productRequestToProduct(product, productRequest)).toDTO();
    }

    public ProductResponse updateProduct(String id, ProductRequest updatedProduct) {
        Product product = this.productDao.getProductByID(id);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }
        Product updated = this.productDao.addProduct(productRequestToProduct(product, updatedProduct));
        return updated.toDTO();
    }

    public void deleteProduct(String id) {
        Product product = this.productDao.getProductByID(id);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }
        this.productDao.deleteProduct(product);
    }

    public Resource getProductImage(String fileName) throws MalformedURLException {
        Path filePath = Paths.get(IMAGE_PATH + "/" + fileName);
        return new UrlResource(filePath.toUri());
    }

    private Product productRequestToProduct(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setDescription(productRequest.getDescription());
        product.setCategory(this.categoryDao.getCategoryByID(productRequest.getCategoryId()));
        try {
            MultipartFile imageFile = productRequest.getImage();
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get(IMAGE_PATH, fileName);

            // Save the image file to the filesystem
            Files.createDirectories(filePath.getParent()); // Ensure directory exists
            Files.copy(imageFile.getInputStream(), filePath);

            // Store the file path in the database
            product.setImage(fileName);
        } catch (Exception e) {
            product.setImage(null);
        }
        return product;
    }
}
