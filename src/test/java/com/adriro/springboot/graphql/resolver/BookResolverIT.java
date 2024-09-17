package com.adriro.springboot.graphql.resolver;

import com.adriro.springboot.graphql.domain.Book;
import com.adriro.springboot.graphql.dto.BookConnectionDto;
import com.adriro.springboot.graphql.dto.BookEdgeDto;
import com.adriro.springboot.graphql.dto.PageInfoDto;
import com.adriro.springboot.graphql.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@GraphQlTest(BookResolver.class)
@SpringJUnitConfig
public class BookResolverIT {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private BookService bookService;


    @BeforeEach
    void setUp() {
        Book book1 = Book.builder().id(1).title("Book 1").build();
        Book book2 = Book.builder().id(2).title("Book 2").build();

        BookEdgeDto edge1 = BookEdgeDto.builder().node(book1).cursor("cursor1").build();
        BookEdgeDto edge2 = BookEdgeDto.builder().node(book2).cursor("cursor2").build();

        PageInfoDto pageInfo = PageInfoDto.builder()
                .hasNextPage(false)
                .endCursor("cursor2")
                .build();

        BookConnectionDto bookConnectionDto = BookConnectionDto.builder()
                .edges(Arrays.asList(edge1, edge2))
                .pageInfo(pageInfo)
                .build();

        when(bookService.findAll()).thenReturn(Arrays.asList(book1, book2));
        when(bookService.findPaginated(any(Integer.class), any(String.class))).thenReturn(bookConnectionDto);
        when(bookService.findById(1)).thenReturn(Optional.of(book1));
        when(bookService.findById(2)).thenReturn(Optional.of(book2));
        when(bookService.findById(any(Integer.class))).thenReturn(Optional.empty());
    }

    @Test
    void testAllBooks() {
        String query = """
                {
                  allBooks {
                    id
                    title
                  }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("allBooks")
                .hasValue()
                .path("allBooks[0].title")
                .entity(String.class)
                .isEqualTo("Book 1")
                .path("allBooks[1].title")
                .entity(String.class)
                .isEqualTo("Book 2");
    }

    @Test
    void testBooksPaginated() {
        String query = """
                query ($first: Int!, $after: String) {
                  booksPaginated(first: $first, after: $after) {
                    edges {
                      node {
                        id
                        title
                      }
                      cursor
                    }
                    pageInfo {
                      hasNextPage
                      endCursor
                    }
                  }
                }
                """;

        graphQlTester.document(query)
                .variable("first", 2)
                .variable("after", "")
                .execute()
                .path("booksPaginated")
                .hasValue()
                .path("booksPaginated.edges[0].node.title")
                .entity(String.class)
                .isEqualTo("Book 1")
                .path("booksPaginated.edges[1].node.title")
                .entity(String.class)
                .isEqualTo("Book 2")
                .path("booksPaginated.pageInfo.hasNextPage")
                .entity(Boolean.class)
                .isEqualTo(false)
                .path("booksPaginated.pageInfo.endCursor")
                .entity(String.class)
                .isEqualTo("cursor2");
    }

    @Test
    void testBookById() {
        Book book1 = Book.builder().id(1).title("Book 1").build();
        String query = """
                query ($id: ID!) {
                  bookById(id: $id) {
                    id
                    title
                  }
                }
                """;

        when(bookService.findById(1)).thenReturn(Optional.of(book1));

        graphQlTester.document(query)
                .variable("id", "1")
                .execute()
                .path("bookById")
                .hasValue()
                .path("bookById.title")
                .entity(String.class)
                .isEqualTo("Book 1");
    }

    @Test
    void testBookByIdThrowsBookNotFoundException() {

        String query = """
                query ($id: ID!) {
                  bookById(id: $id) {
                    id
                    title
                  }
                }
                """;

        graphQlTester.document(query)
                .variable("id", "99")
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).hasSize(1);
                    assertThat(errors.get(0).getMessage()).contains("Book not found with ID: 99");
                });
    }
}
