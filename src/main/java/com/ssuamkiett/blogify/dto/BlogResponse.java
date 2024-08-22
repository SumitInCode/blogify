package com.ssuamkiett.blogify.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogResponse {
    private Long id;
    private String title;
    private String body;
    private String author;
    private Double rate;
    private String tags;
}
