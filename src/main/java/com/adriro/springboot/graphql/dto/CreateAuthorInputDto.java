package com.adriro.springboot.graphql.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateAuthorInputDto {

    private String name;
}
