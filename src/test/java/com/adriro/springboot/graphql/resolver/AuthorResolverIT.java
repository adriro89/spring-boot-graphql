package com.adriro.springboot.graphql.resolver;

import com.adriro.springboot.graphql.domain.Author;
import com.adriro.springboot.graphql.dto.CreateAuthorInputDto;
import com.adriro.springboot.graphql.dto.UpdateAuthorInputDto;
import com.adriro.springboot.graphql.exception.AuthorNotFoundException;
import com.adriro.springboot.graphql.service.AuthorService;
import com.adriro.springboot.graphql.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@GraphQlTest(AuthorResolver.class)
@SpringJUnitConfig
public class AuthorResolverIT {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private BookService bookService;

    @BeforeEach
    void setUp() {
        Author author1 = Author.builder().id(1).name("Author 1").books(new ArrayList<>()).build();
        Author author2 = Author.builder().id(2).name("Author 2").books(new ArrayList<>()).build();

        when(authorService.findAll()).thenReturn(Arrays.asList(author1, author2));
        when(bookService.findAuthorByBookId(1)).thenReturn(Optional.of(author1));

        when(authorService.createAuthor(any(CreateAuthorInputDto.class)))
                .thenAnswer(invocation -> {
                    CreateAuthorInputDto input = invocation.getArgument(0);
                    return Author.builder()
                            .id(3)
                            .name(input.getName())
                            .books(new ArrayList<>())
                            .build();
                });

        when(authorService.updateAuthor(any(Integer.class), any(UpdateAuthorInputDto.class)))
                .thenAnswer(invocation -> {
                    Integer id = invocation.getArgument(0);
                    UpdateAuthorInputDto input = invocation.getArgument(1);
                    return Author.builder()
                            .id(id)
                            .name(input.getName())
                            .books(new ArrayList<>())
                            .build();
                });

        when(authorService.deleteAuthor(any(Integer.class))).thenReturn(true);
    }

    @Test
    void testAllAuthors() {
        String query = """
                {
                  allAuthors {
                    id
                    name
                  }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("allAuthors")
                .hasValue()
                .path("allAuthors[0].name")
                .entity(String.class)
                .isEqualTo("Author 1")
                .path("allAuthors[1].name")
                .entity(String.class)
                .isEqualTo("Author 2");
    }

    @Test
    void testAuthorByBook() {
        String query = """
                query ($bookId: ID!) {
                  authorByBook(bookId: $bookId) {
                    id
                    name
                  }
                }
                """;

        graphQlTester.document(query)
                .variable("bookId", "1")
                .execute()
                .path("authorByBook")
                .hasValue()
                .path("authorByBook.name")
                .entity(String.class)
                .isEqualTo("Author 1");
    }

    @Test
    void testCreateAuthor() {
        String mutation = """
                mutation ($input: CreateAuthorInput!) {
                  createAuthor(input: $input) {
                    id
                    name
                  }
                }
                """;

        Map<String, Object> input = Map.of("name", "New Author");

        graphQlTester.document(mutation)
                .variable("input", input)
                .execute()
                .path("createAuthor")
                .hasValue()
                .path("createAuthor.name")
                .entity(String.class)
                .isEqualTo("New Author");
    }

    @Test
    void testUpdateAuthor() {
        String mutation = """
                mutation ($id: ID!, $input: UpdateAuthorInput!) {
                  updateAuthor(id: $id, input: $input) {
                    id
                    name
                  }
                }
                """;

        Map<String, Object> input = Map.of("name", "Updated Author");

        graphQlTester.document(mutation)
                .variable("id", "1")
                .variable("input", input)
                .execute()
                .path("updateAuthor")
                .hasValue()
                .path("updateAuthor.name")
                .entity(String.class)
                .isEqualTo("Updated Author");
    }

    @Test
    void testDeleteAuthor() {
        String mutation = """
                mutation ($id: ID!) {
                  deleteAuthor(id: $id)
                }
                """;

        graphQlTester.document(mutation)
                .variable("id", "1")
                .execute()
                .path("deleteAuthor")
                .entity(Boolean.class)
                .isEqualTo(Boolean.TRUE);
    }

    @Test
    void testUpdateAuthorThrowsAuthorNotFoundException() {
        when(authorService.updateAuthor(any(Integer.class), any(UpdateAuthorInputDto.class)))
                .thenThrow(new AuthorNotFoundException(1));

        String mutation = """
                mutation ($id: ID!, $input: UpdateAuthorInput!) {
                  updateAuthor(id: $id, input: $input) {
                    id
                    name
                  }
                }
                """;

        graphQlTester.document(mutation)
                .variable("id", "1")
                .variable("input", Map.of("name", "Updated Author"))
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).hasSize(1);
                    assertThat(errors.get(0).getMessage()).contains("Author not found with ID: 1");
                });
    }

    @Test
    void testDeleteAuthorThrowsAuthorNotFoundException() {
        when(authorService.deleteAuthor(any(Integer.class)))
                .thenThrow(new AuthorNotFoundException(1));

        String mutation = """
                mutation ($id: ID!) {
                  deleteAuthor(id: $id)
                }
                """;

        graphQlTester.document(mutation)
                .variable("id", "1")
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).hasSize(1);
                    assertThat(errors.get(0).getMessage()).contains("Author not found with ID: 1");
                });
    }
}
