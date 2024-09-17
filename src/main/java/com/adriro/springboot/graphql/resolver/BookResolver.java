package com.adriro.springboot.graphql.resolver;

import com.adriro.springboot.graphql.domain.Book;
import com.adriro.springboot.graphql.dto.BookConnectionDto;
import com.adriro.springboot.graphql.exception.BookNotFoundException;
import com.adriro.springboot.graphql.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookResolver {

    private final BookService bookService;

    @QueryMapping
    public List<Book> allBooks() {
        return bookService.findAll();
    }

    @QueryMapping
    public BookConnectionDto booksPaginated(@Argument Integer first, @Argument String after) {
        return bookService.findPaginated(first, after);
    }

    @QueryMapping
    public Book bookById(@Argument Integer id) {
        return bookService.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }
}
