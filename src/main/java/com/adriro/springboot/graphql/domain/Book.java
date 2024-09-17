package com.adriro.springboot.graphql.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Book {

    private Integer id;
    private String title;
    private Author author;
}
