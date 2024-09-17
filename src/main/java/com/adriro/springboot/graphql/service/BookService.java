package com.adriro.springboot.graphql.service;

import com.adriro.springboot.graphql.domain.Author;
import com.adriro.springboot.graphql.domain.Book;
import com.adriro.springboot.graphql.dto.BookConnectionDto;
import com.adriro.springboot.graphql.dto.BookEdgeDto;
import com.adriro.springboot.graphql.dto.PageInfoDto;
import com.adriro.springboot.graphql.entity.BookEntity;
import com.adriro.springboot.graphql.exception.BookNotFoundException;
import com.adriro.springboot.graphql.mapper.BookMapper;
import com.adriro.springboot.graphql.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.adriro.springboot.graphql.mapper.BookMapper.toDomain;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;


    public List<Book> findAll() {
        return bookRepository.findAll().stream()
                .map(BookMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Optional<Book> findById(Integer id) {
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return Optional.of(toDomain(bookEntity));
    }

    public BookConnectionDto findPaginated(Integer first, String after) {
        int startIndex = (after == null) ? 0 : Integer.parseInt(after);
        Pageable pageable = PageRequest.of(startIndex / first, first);

        Slice<BookEntity> bookSlice = bookRepository.findAll(pageable);
        List<Book> paginatedBooks = bookSlice.getContent().stream()
                .map(BookMapper::toDomain)
                .toList();

        List<BookEdgeDto> edges = paginatedBooks.stream()
                .map(book -> new BookEdgeDto(String.valueOf(startIndex + paginatedBooks.indexOf(book) + 1), book))
                .collect(Collectors.toList());

        PageInfoDto pageInfo = PageInfoDto.builder()
                .hasNextPage(bookSlice.hasNext())
                .endCursor(edges.isEmpty() ? null : edges.get(edges.size() - 1).getCursor())
                .build();

        return new BookConnectionDto(edges, pageInfo);
    }

    public Optional<Author> findAuthorByBookId(Integer bookId) {
        return findById(bookId).map(Book::getAuthor);
    }
}