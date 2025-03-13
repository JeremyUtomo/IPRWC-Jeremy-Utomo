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

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

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
