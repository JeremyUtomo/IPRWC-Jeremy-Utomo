package com.example.iprwcspringbootjeremy.DAO;

import com.example.iprwcspringbootjeremy.Model.Order;
import com.example.iprwcspringbootjeremy.Repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderDao {
    private final OrderRepository orderRepository;

    public OrderDao(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getOrdersByUser(String userId) {
        return this.orderRepository.findByUserId(UUID.fromString(userId));
    }

    public Order addOrder(Order order) {
        return this.orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }
}
