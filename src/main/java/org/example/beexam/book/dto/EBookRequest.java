package org.example.beexam.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EBookRequest {
    @NotBlank private String isbn;
    @NotBlank private String title;
    private Integer publishYear;
    @NotNull @Min(1) private Integer totalCopies;

    @NotNull private Long authorId;
    @NotNull private Long categoryId;

    @NotBlank private String fileFormat;
    @NotBlank private String fileUrl;
}