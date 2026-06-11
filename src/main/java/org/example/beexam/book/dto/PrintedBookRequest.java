package org.example.beexam.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrintedBookRequest {
    @NotBlank(message = "ISBN obbligatorio")
    private String isbn;
    @NotBlank(message = "Titolo obbligatorio")
    private String title;
    private Integer publishYear;
    @NotNull @Min(1)
    private Integer totalCopies;

    @NotNull(message = "ID Autore obbligatorio")
    private Long authorId;
    @NotNull(message = "ID Categoria obbligatorio")
    private Long categoryId;

    @NotBlank(message = "Posizione fisica obbligatoria")
    private String physicalLocation;
    @NotNull @Min(1)
    private Integer pages;
}