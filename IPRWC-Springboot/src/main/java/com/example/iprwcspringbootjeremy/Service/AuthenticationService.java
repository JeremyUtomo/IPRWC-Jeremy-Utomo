package com.example.iprwcspringbootjeremy.Service;

import com.example.iprwcspringbootjeremy.DTO.Request.AuthenticationRequest;
import com.example.iprwcspringbootjeremy.DTO.Response.AuthenticationResponse;
import com.example.iprwcspringbootjeremy.DTO.Request.RegisterRequest;
import com.example.iprwcspringbootjeremy.Exception.*;
import com.example.iprwcspringbootjeremy.Model.*;
import com.example.iprwcspringbootjeremy.Repository.*;
import lombok.*;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private boolean validateEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    private boolean validatePassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpperCase = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasLowerCase = Pattern.compile("[a-z]").matcher(password).find();
        boolean hasNumber = Pattern.compile("[0-9]").matcher(password).find();
        boolean hasSpecialChar = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]").matcher(password).find();
        
        return hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        // Validate email format
        if (!validateEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email must contain @ and . characters.");
        }

        // Validate password requirements
        if (!validatePassword(request.getPassword())) {
            throw new IllegalArgumentException("Password must be at least 8 characters and contain uppercase, lowercase, number, and special character.");
        }

        if (repository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistException("Email already used. ");
        }

        var user = User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Validate email format
        if (!validateEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email must contain @ and . characters.");
        }

        // Validate password requirements
        if (!validatePassword(request.getPassword())) {
            throw new IllegalArgumentException("Password must be at least 8 characters and contain uppercase, lowercase, number, and special character.");
        }
        
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
            .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }
}