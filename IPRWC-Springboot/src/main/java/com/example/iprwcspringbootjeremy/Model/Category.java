package com.example.iprwcspringbootjeremy.Model;

import com.example.iprwcspringbootjeremy.DTO.CategoryDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products;

    public CategoryDTO toDTO() {
        return CategoryDTO.builder()
                .id(id)
                .name(name)
                .build();
    }
}