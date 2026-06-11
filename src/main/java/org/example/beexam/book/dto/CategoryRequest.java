package org.example.beexam.book.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank(message = "Il nome della categoria è obbligatorio")
    private String name;
    private String description;
}