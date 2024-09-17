package com.adriro.springboot.graphql.resolver;

import com.adriro.springboot.graphql.domain.Author;
import com.adriro.springboot.graphql.dto.CreateAuthorInputDto;
import com.adriro.springboot.graphql.dto.UpdateAuthorInputDto;
import com.adriro.springboot.graphql.service.AuthorService;
import com.adriro.springboot.graphql.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorResolver {

    private final BookService bookService;
    private final AuthorService authorService;

    @QueryMapping
    public List<Author> allAuthors() {
        return authorService.findAll();
    }

    @QueryMapping
    public Author authorByBook(@Argument Integer bookId) {
        return bookService.findAuthorByBookId(bookId).orElse(null);
    }

    @MutationMapping
    public Author createAuthor(@Argument CreateAuthorInputDto input) {
        return authorService.createAuthor(input);
    }

    @MutationMapping
    public Author updateAuthor(@Argument Integer id, @Argument UpdateAuthorInputDto input) {
        return authorService.updateAuthor(id, input);
    }

    @MutationMapping
    public boolean deleteAuthor(@Argument Integer id) {
        return authorService.deleteAuthor(id);
    }
}
