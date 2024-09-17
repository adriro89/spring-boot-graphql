package com.adriro.springboot.graphql.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class BookConnectionDto {

    private List<BookEdgeDto> edges;
    private PageInfoDto pageInfo;
}
