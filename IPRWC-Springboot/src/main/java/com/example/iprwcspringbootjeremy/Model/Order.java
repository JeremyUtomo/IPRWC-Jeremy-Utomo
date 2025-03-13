package com.example.iprwcspringbootjeremy.Model;

import com.example.iprwcspringbootjeremy.DTO.Response.OrderResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Date orderDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    private Double total;

    public OrderResponse toResponseDTO() {
        return OrderResponse.builder()
                .id(id)
                .orderDate(orderDate)
                .userId(user.getId().toString())
                .products(products)
                .total(total)
                .build();
    }
}
