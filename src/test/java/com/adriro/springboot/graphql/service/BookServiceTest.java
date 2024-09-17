package com.adriro.springboot.graphql.service;

import com.adriro.springboot.graphql.domain.Author;
import com.adriro.springboot.graphql.domain.Book;
import com.adriro.springboot.graphql.dto.BookConnectionDto;
import com.adriro.springboot.graphql.entity.AuthorEntity;
import com.adriro.springboot.graphql.entity.BookEntity;
import com.adriro.springboot.graphql.exception.BookNotFoundException;
import com.adriro.springboot.graphql.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void testFindAll() {
        BookEntity book1 = BookEntity.builder().id(1).title("Book 1").build();
        BookEntity book2 = BookEntity.builder().id(2).title("Book 2").build();
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<Book> books = bookService.findAll();

        assertThat(books).hasSize(2);
        assertThat(books.get(0).getTitle()).isEqualTo("Book 1");
        assertThat(books.get(1).getTitle()).isEqualTo("Book 2");
    }

    @Test
    void testFindById() {
        BookEntity bookEntity = BookEntity.builder().id(1).title("Book 1").build();
        when(bookRepository.findById(1)).thenReturn(Optional.of(bookEntity));

        Optional<Book> book = bookService.findById(1);

        assertThat(book).isPresent();
        assertThat(book.get().getId()).isEqualTo(1);
        assertThat(book.get().getTitle()).isEqualTo("Book 1");
    }

    @Test
    void testFindByIdThrowsBookNotFoundException() {
        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findById(1))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("Book not found with ID: 1");
    }

    @Test
    void testFindPaginated() {
        BookEntity book1 = BookEntity.builder().id(1).title("Book 1").build();
        BookEntity book2 = BookEntity.builder().id(2).title("Book 2").build();
        List<BookEntity> bookEntities = Arrays.asList(book1, book2);
        Page<BookEntity> page = new PageImpl<>(bookEntities);
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(page);

        BookConnectionDto result = bookService.findPaginated(2, null);

        assertThat(result).isNotNull();
        assertThat(result.getEdges()).hasSize(2);
        assertThat(result.getEdges().get(0).getNode().getTitle()).isEqualTo("Book 1");
        assertThat(result.getEdges().get(1).getNode().getTitle()).isEqualTo("Book 2");
        assertThat(result.getPageInfo().isHasNextPage()).isFalse();
    }

    @Test
    void testFindAuthorByBookId() {
        AuthorEntity author = AuthorEntity.builder().id(1).name("Author 1").build();
        BookEntity bookEntity = BookEntity.builder().id(1).title("Book 1").author(author).build();
        when(bookRepository.findById(1)).thenReturn(Optional.of(bookEntity));

        Optional<Author> foundAuthor = bookService.findAuthorByBookId(1);

        assertThat(foundAuthor).isPresent();
        assertThat(foundAuthor.get().getName()).isEqualTo("Author 1");
    }

    @Test
    void testFindAuthorByBookIdThrowsBookNotFoundException() {
        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findAuthorByBookId(1))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("Book not found with ID: 1");
    }
}
