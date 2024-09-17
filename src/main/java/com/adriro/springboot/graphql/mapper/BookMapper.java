package com.adriro.springboot.graphql.mapper;

import com.adriro.springboot.graphql.domain.Author;
import com.adriro.springboot.graphql.domain.Book;
import com.adriro.springboot.graphql.entity.AuthorEntity;
import com.adriro.springboot.graphql.entity.BookEntity;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BookMapper {

    public static Book toDomain(BookEntity bookEntity) {
        if (bookEntity == null) {
            return null;
        }
        Author author = toDomain(bookEntity.getAuthor());
        return Book.builder()
                .id(bookEntity.getId())
                .title(bookEntity.getTitle())
                .author(author)
                .build();
    }

    private static Author toDomain(AuthorEntity authorEntity) {
        if (authorEntity == null) {
            return null;
        }
        return Author.builder()
                .id(authorEntity.getId())
                .name(authorEntity.getName())
                .build();
    }

    public BookEntity toEntity(Book book) {
        if (book == null) {
            return null;
        }
        AuthorEntity authorEntity = toEntity(book.getAuthor());
        return BookEntity.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(authorEntity)
                .build();
    }

    private AuthorEntity toEntity(Author author) {
        if (author == null) {
            return null;
        }
        List<BookEntity> books = author.getBooks().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        return AuthorEntity.builder()
                .id(author.getId())
                .name(author.getName())
                .books(books)
                .build();
    }
}
