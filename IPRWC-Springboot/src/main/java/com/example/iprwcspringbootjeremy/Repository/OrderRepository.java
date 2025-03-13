package com.example.iprwcspringbootjeremy.Repository;

import com.example.iprwcspringbootjeremy.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUserId(UUID user_id);
}
