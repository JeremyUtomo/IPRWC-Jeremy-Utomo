package com.example.iprwcspringbootjeremy.Service;

import com.example.iprwcspringbootjeremy.DAO.OrderDao;
import com.example.iprwcspringbootjeremy.DAO.ProductDao;
import com.example.iprwcspringbootjeremy.DAO.UserDao;
import com.example.iprwcspringbootjeremy.DTO.Request.OrderRequest;
import com.example.iprwcspringbootjeremy.DTO.Response.OrderResponse;
import com.example.iprwcspringbootjeremy.Model.Order;
import com.example.iprwcspringbootjeremy.Model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    public OrderService(OrderDao orderDao, UserDao userDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }


    public List<OrderResponse> getOrdersByUser(String userId) {
        List<Order> orders = this.orderDao.getOrdersByUser(userId);
        List<OrderResponse> orderResponses = new ArrayList<>(orders.size());
        for (Order order : orders) {
            orderResponses.add(order.toResponseDTO());
        }
        return orderResponses;
    }

    public OrderResponse addOrder(OrderRequest orderRequest) {
        double total = 0;
        List<Product> products = new ArrayList<>(orderRequest.getProductIds().size());
        for (UUID productId : orderRequest.getProductIds()) {
            Product product = this.productDao.getProductByID(productId);

            if (product.getStock() == 0) {
                throw new IllegalArgumentException("Product out of stock");
            }

            products.add(product);
            total += product.getPrice();

            product.setStock(product.getStock() - 1);
            this.productDao.addProduct(product);
        }

        Order order = new Order();
        order.setUser(this.userDao.getById(orderRequest.getUserId()));
        order.setProducts(products);
        order.setTotal(total);
        order.setOrderDate(new java.util.Date());

        return this.orderDao.addOrder(order).toResponseDTO();
    }

    public void removeProductFromAllOrders(String productId) {
        List<Order> orders = this.orderDao.getAllOrders();
        Product product = this.productDao.getProductByID(productId);

        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        for (Order order : orders) {
            if (order.getProducts().contains(product)) {
                order.getProducts().remove(product);
                order.setTotal(order.getTotal() - product.getPrice());
                this.orderDao.addOrder(order);
            }
        }

        product.setStock(product.getStock() + orders.size());
        this.productDao.addProduct(product);
    }
}
