package com.ssuamkiett.blogify.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BlogRequest(
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String title,
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String body,
        String tags
) {
}
