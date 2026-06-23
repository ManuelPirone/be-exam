package org.example.beexam.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryBookCountResponse {
    private String categoryName;
    private Long bookCount;
}