package org.example.beexam.loan.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanRequest {
    @NotNull(message = "L'ID del libro è obbligatorio")
    private Long bookId;
}