package com.adriro.springboot.graphql.service;

import com.adriro.springboot.graphql.domain.Author;
import com.adriro.springboot.graphql.dto.CreateAuthorInputDto;
import com.adriro.springboot.graphql.dto.UpdateAuthorInputDto;
import com.adriro.springboot.graphql.entity.AuthorEntity;
import com.adriro.springboot.graphql.exception.AuthorNotFoundException;
import com.adriro.springboot.graphql.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void testFindAll() {
        AuthorEntity author1 = AuthorEntity.builder().id(1).name("Author 1").build();
        AuthorEntity author2 = AuthorEntity.builder().id(2).name("Author 2").build();
        when(authorRepository.findAll()).thenReturn(Arrays.asList(author1, author2));

        List<Author> authors = authorService.findAll();

        assertThat(authors).hasSize(2);
        assertThat(authors.get(0).getName()).isEqualTo("Author 1");
        assertThat(authors.get(1).getName()).isEqualTo("Author 2");
    }

    @Test
    void testCreateAuthor() {
        CreateAuthorInputDto input = CreateAuthorInputDto.builder().name("New Author").build();
        AuthorEntity authorEntity = AuthorEntity.builder().id(1).name("New Author").build();
        when(authorRepository.save(any(AuthorEntity.class))).thenReturn(authorEntity);

        Author author = authorService.createAuthor(input);

        assertThat(author.getName()).isEqualTo("New Author");
    }

    @Test
    void testUpdateAuthor() {
        AuthorEntity authorEntity = AuthorEntity.builder().id(1).name("Old Name").build();
        UpdateAuthorInputDto input = UpdateAuthorInputDto.builder().name("New Name").build();
        when(authorRepository.findById(1)).thenReturn(Optional.of(authorEntity));
        when(authorRepository.save(authorEntity)).thenReturn(authorEntity);

        Author updatedAuthor = authorService.updateAuthor(1, input);

        assertThat(updatedAuthor.getName()).isEqualTo("New Name");
    }

    @Test
    void testUpdateAuthorThrowsAuthorNotFoundException() {
        UpdateAuthorInputDto input = UpdateAuthorInputDto.builder().name("New Name").build();
        when(authorRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.updateAuthor(1, input))
                .isInstanceOf(AuthorNotFoundException.class)
                .hasMessageContaining("Author not found with ID: 1");
    }

    @Test
    void testDeleteAuthor() {
        when(authorRepository.existsById(1)).thenReturn(true);

        boolean result = authorService.deleteAuthor(1);

        assertThat(result).isTrue();
    }

    @Test
    void testDeleteAuthorThrowsAuthorNotFoundException() {
        when(authorRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> authorService.deleteAuthor(1))
                .isInstanceOf(AuthorNotFoundException.class)
                .hasMessageContaining("Author not found with ID: 1");
    }
}
