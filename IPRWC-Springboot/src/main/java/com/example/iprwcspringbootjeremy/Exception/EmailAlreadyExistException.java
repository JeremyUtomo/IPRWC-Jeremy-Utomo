package com.example.iprwcspringbootjeremy.Exception;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(String s) {
        super(s);
    }
}
