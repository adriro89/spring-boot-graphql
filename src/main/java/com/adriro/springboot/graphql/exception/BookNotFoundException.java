package com.adriro.springboot.graphql.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Integer id) {
        super("Book not found with ID: " + id);
    }
}
