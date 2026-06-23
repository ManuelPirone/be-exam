package org.example.beexam.book.controller;

import lombok.RequiredArgsConstructor;
import org.example.beexam.book.dto.BookResponse;
import org.example.beexam.book.dto.CategoryBookCountResponse;
import org.example.beexam.book.dto.EBookRequest;
import org.example.beexam.book.dto.PrintedBookRequest;
import org.example.beexam.book.service.BookService;
import org.example.beexam.book.service.OpenLibraryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final OpenLibraryService openLibraryService;

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

    @GetMapping("/metadata/{isbn}")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getBookMetadata(@PathVariable String isbn) {
        Map<String, Object> metadata = openLibraryService.fetchBookMetadata(isbn);
        return ResponseEntity.ok(metadata);
    }

    @GetMapping("/statistics/categories")
    public ResponseEntity<List<CategoryBookCountResponse>> getBooksCountByCategory() {
        return ResponseEntity.ok(bookService.getBooksCountByCategory());
    }

    @GetMapping("/statistics/total")
    public ResponseEntity<Map<String, Long>> getTotalBooksCount() {
        long total = bookService.getTotalBooksCount();
        return ResponseEntity.ok(Map.of("totalBooks", total));
    }
}