package com.example.iprwcspringbootjeremy.DTO.Response;

import com.example.iprwcspringbootjeremy.Model.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
}
