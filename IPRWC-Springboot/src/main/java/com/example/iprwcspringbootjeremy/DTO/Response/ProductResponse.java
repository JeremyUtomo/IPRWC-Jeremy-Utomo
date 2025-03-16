package com.example.iprwcspringbootjeremy.DTO.Response;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String image;
    private String categoryId;
}