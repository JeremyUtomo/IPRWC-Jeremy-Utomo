package com.example.iprwcspringbootjeremy.DTO.Response;

import org.springframework.http.*;

public class ApiResponse<Type> {
    private HttpStatus code;
    private Type payload;
    private String message;
    private int status;


    public ApiResponse(HttpStatus code, Type payload) {
        this.code = code;
        if (code == HttpStatus.ACCEPTED) {
            this.payload = payload;
        }
    }

    public ApiResponse(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResponse(HttpStatus code, Type payload, String message) {
        this.code = code;
        this.message = message;
    }
}
