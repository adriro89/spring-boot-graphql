package com.adriro.springboot.graphql.dto;

import com.adriro.springboot.graphql.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BookEdgeDto {

    private String cursor;
    private Book node;
}
