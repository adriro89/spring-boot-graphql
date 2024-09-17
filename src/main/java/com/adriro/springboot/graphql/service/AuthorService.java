package com.adriro.springboot.graphql.service;

import com.adriro.springboot.graphql.domain.Author;
import com.adriro.springboot.graphql.dto.CreateAuthorInputDto;
import com.adriro.springboot.graphql.dto.UpdateAuthorInputDto;
import com.adriro.springboot.graphql.entity.AuthorEntity;
import com.adriro.springboot.graphql.exception.AuthorNotFoundException;
import com.adriro.springboot.graphql.mapper.AuthorMapper;
import com.adriro.springboot.graphql.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.adriro.springboot.graphql.mapper.AuthorMapper.toDomain;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<Author> findAll() {
        return authorRepository.findAll().stream()
                .map(AuthorMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Author createAuthor(CreateAuthorInputDto input) {
        AuthorEntity authorEntity = AuthorEntity.builder()
                .name(input.getName())
                .build();
        AuthorEntity savedEntity = authorRepository.save(authorEntity);
        return toDomain(savedEntity);
    }

    public Author updateAuthor(Integer id, UpdateAuthorInputDto input) {
        AuthorEntity author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));

        if (input.getName() != null) {
            author.setName(input.getName());
        }
        AuthorEntity updatedEntity = authorRepository.save(author);
        return toDomain(updatedEntity);
    }

    public boolean deleteAuthor(Integer id) {
        if (!authorRepository.existsById(id)) {
            throw new AuthorNotFoundException(id);
        }
        authorRepository.deleteById(id);
        return true;
    }
    
}
