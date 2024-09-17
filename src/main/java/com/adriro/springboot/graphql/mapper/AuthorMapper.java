package com.adriro.springboot.graphql.mapper;

import com.adriro.springboot.graphql.domain.Author;
import com.adriro.springboot.graphql.domain.Book;
import com.adriro.springboot.graphql.entity.AuthorEntity;

import java.util.List;

public abstract class AuthorMapper {

    public static Author toDomain(AuthorEntity entity) {
        List<Book> books = entity.getBooks() != null ?
                entity.getBooks().stream()
                        .map(book -> Book.builder()
                                .id(book.getId())
                                .title(book.getTitle())
                                .build())
                        .toList() : List.of();

        return Author.builder()
                .id(entity.getId())
                .name(entity.getName())
                .books(books)
                .build();
    }
}
