package com.example.iprwcspringbootjeremy.Service;

import com.example.iprwcspringbootjeremy.DAO.CategoryDao;
import com.example.iprwcspringbootjeremy.DAO.ProductDao;
import com.example.iprwcspringbootjeremy.DTO.Request.ProductRequest;
import com.example.iprwcspringbootjeremy.DTO.Response.ProductResponse;
import com.example.iprwcspringbootjeremy.Model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ProductService {

    private final ProductDao productDao;
    private final CategoryDao categoryDao;

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

    private Product productRequestToProduct(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setDescription(productRequest.getDescription());
        product.setCategory(this.categoryDao.getCategoryByID(productRequest.getCategoryId()));
        try {
            product.setImage(Base64.getDecoder().decode(productRequest.getImage()));
        } catch (Exception e) {
            product.setImage(null);
        }
        return product;
    }
}
