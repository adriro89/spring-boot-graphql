package com.adriro.springboot.graphql.config;

import com.adriro.springboot.graphql.entity.AuthorEntity;
import com.adriro.springboot.graphql.entity.BookEntity;
import com.adriro.springboot.graphql.repository.AuthorRepository;
import com.adriro.springboot.graphql.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DatabaseDataInitializer {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @PostConstruct
    public void init() {
        AuthorEntity jkRowling = AuthorEntity.builder().name("J. K. Rowling").build();
        AuthorEntity tolkien = AuthorEntity.builder().name("J. R. R. Tolkien").build();

        authorRepository.saveAll(Arrays.asList(jkRowling, tolkien));

        BookEntity harryPotter = BookEntity.builder().title("Harry Potter").author(jkRowling).build();
        BookEntity lotr = BookEntity.builder().title("Lord of the Rings").author(tolkien).build();

        bookRepository.saveAll(Arrays.asList(harryPotter, lotr));
    }
}
