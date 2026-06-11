package org.example.beexam.book.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthorRequest {
    @NotBlank(message = "Il nome dell'autore è obbligatorio")
    private String name;
    private String bio;
}