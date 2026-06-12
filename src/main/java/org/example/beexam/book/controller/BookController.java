package org.example.beexam.book.controller;

import lombok.RequiredArgsConstructor;
import org.example.beexam.book.dto.BookResponse;
import org.example.beexam.book.dto.EBookRequest;
import org.example.beexam.book.dto.PrintedBookRequest;
import org.example.beexam.book.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String title
    ) {
        Page<BookResponse> response = bookService.getAllBooks(page, size, sortBy, title);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/printed")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<BookResponse> createPrintedBook(@Validated @RequestBody PrintedBookRequest request) {
        BookResponse response = bookService.createPrintedBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/ebook")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<BookResponse> createEBook(@Validated @RequestBody EBookRequest request) {
        BookResponse response = bookService.createEBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}