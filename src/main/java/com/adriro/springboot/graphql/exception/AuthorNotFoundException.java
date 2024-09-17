package com.adriro.springboot.graphql.exception;

public class AuthorNotFoundException extends RuntimeException {

    public AuthorNotFoundException(Integer id) {
        super("Author not found with ID: " + id);
    }
}
