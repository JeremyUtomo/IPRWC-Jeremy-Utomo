package com.example.iprwcspringbootjeremy.Model;

import com.example.iprwcspringbootjeremy.DTO.Response.ProductResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public ProductResponse toDTO() {
        return ProductResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .image(image)
                .categoryId(category.getId().toString())
                .build();
    }
}