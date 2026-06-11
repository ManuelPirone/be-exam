package org.example.beexam.book.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponse {
    private Long id;
    private String isbn;
    private String title;

    private String bookType;

    private String authorName;
    private String categoryName;

    private Integer publishYear;
    private Integer totalCopies;
    private Integer availableCopies;

    // cartaceo - null se ebook
    private String physicalLocation;
    private Integer pages;

    // ebook - null se cartaceo
    private String fileFormat;
    private String fileUrl;
}