package com.example.iprwcspringbootjeremy.DTO.Response;

import com.example.iprwcspringbootjeremy.Model.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class OrderResponse {
    private UUID id;
    private Date orderDate;
    private String userId;
    private List<Product> products;
    private Double total;
}
