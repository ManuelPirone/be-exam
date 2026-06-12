package org.example.beexam.loan.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class LoanResponse {
    private Long id;
    private String userEmail;
    private String bookTitle;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status; // "ACTIVE" o "RETURNED"
}