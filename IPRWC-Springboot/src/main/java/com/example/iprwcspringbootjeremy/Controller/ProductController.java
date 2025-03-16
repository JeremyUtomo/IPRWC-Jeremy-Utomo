package com.example.iprwcspringbootjeremy.Controller;

import com.example.iprwcspringbootjeremy.DTO.Request.ProductRequest;
import com.example.iprwcspringbootjeremy.DTO.Response.ProductResponse;
import com.example.iprwcspringbootjeremy.Model.*;
import com.example.iprwcspringbootjeremy.Service.OrderService;
import com.example.iprwcspringbootjeremy.Service.ProductService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/product")
public class ProductController {

    private final ProductService productService;
    private final OrderService orderService;

    @Autowired
    public ProductController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(this.productService.getAllProducts());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ProductResponse> getProductByID(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(this.productService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ProductResponse> getProductByName(@PathVariable("name") String name) {
        try {
            return ResponseEntity.ok(this.productService.getByName(name));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ProductResponse> addProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("stock") Integer stock,
            @RequestParam("image") MultipartFile image,
            @RequestParam("categoryId") String categoryId) {
        ProductRequest productRequest = ProductRequest.builder()
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .image(image)
                .categoryId(categoryId)
                .build();
        return ResponseEntity.ok(this.productService.addProduct(productRequest));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable("id") String id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("stock") Integer stock,
            @RequestParam("image") MultipartFile image,
            @RequestParam("categoryId") String categoryId) {
        ProductRequest productRequest = ProductRequest.builder()
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .image(image)
                .categoryId(categoryId)
                .build();
        try {
            return ResponseEntity.ok(this.productService.updateProduct(id, productRequest));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteProduct(@PathVariable("id") String id) {
        try {
            this.orderService.removeProductFromAllOrders(id);
            this.productService.deleteProduct(id);
        } catch (Exception e) {
            return;
        }
    }

    @RequestMapping(value = "/image/{fileName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) throws MalformedURLException {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(this.productService.getProductImage(fileName));
    }
}