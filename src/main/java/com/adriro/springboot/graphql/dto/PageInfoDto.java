package com.adriro.springboot.graphql.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageInfoDto {

    private boolean hasNextPage;
    private String endCursor;
}
