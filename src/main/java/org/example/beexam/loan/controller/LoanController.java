package org.example.beexam.loan.controller;

import lombok.RequiredArgsConstructor;
import org.example.beexam.loan.dto.LoanRequest;
import org.example.beexam.loan.dto.LoanResponse;
import org.example.beexam.loan.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/borrow")
    public ResponseEntity<LoanResponse> borrowBook(
            @Validated @RequestBody LoanRequest request,
            Principal principal
    ) {
        LoanResponse response = loanService.borrowBook(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<LoanResponse> returnBook(
            @PathVariable Long id,
            Principal principal
    ) {
        LoanResponse response = loanService.returnBook(id, principal.getName());
        return ResponseEntity.ok(response);
    }
}