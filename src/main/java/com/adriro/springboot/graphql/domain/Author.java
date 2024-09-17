package com.adriro.springboot.graphql.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Author {

    private Integer id;
    private String name;
    private List<Book> books;
}
