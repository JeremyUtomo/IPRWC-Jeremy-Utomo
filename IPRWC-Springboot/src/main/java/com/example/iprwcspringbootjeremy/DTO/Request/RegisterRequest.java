package com.example.iprwcspringbootjeremy.DTO.Request;

import com.example.iprwcspringbootjeremy.Model.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}
