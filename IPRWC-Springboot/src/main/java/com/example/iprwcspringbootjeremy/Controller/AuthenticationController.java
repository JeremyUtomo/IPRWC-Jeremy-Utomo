package com.example.iprwcspringbootjeremy.Controller;

import com.example.iprwcspringbootjeremy.DTO.Request.AuthenticationRequest;
import com.example.iprwcspringbootjeremy.DTO.Response.AuthenticationResponse;
import com.example.iprwcspringbootjeremy.DTO.Request.RegisterRequest;
import com.example.iprwcspringbootjeremy.Service.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
     ) {
         return ResponseEntity.ok(service.register(request));
     }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
     ) {
         return ResponseEntity.ok(service.authenticate(request));
     }
}
