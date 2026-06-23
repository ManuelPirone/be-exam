package org.example.beexam.book.service;

import lombok.RequiredArgsConstructor;
import org.example.beexam.book.dto.BookResponse;
import org.example.beexam.book.dto.CategoryBookCountResponse;
import org.example.beexam.book.dto.EBookRequest;
import org.example.beexam.book.dto.PrintedBookRequest;
import org.example.beexam.book.entity.Author;
import org.example.beexam.book.entity.Book;
import org.example.beexam.book.entity.Category;
import org.example.beexam.book.entity.EBook;
import org.example.beexam.book.entity.PrintedBook;
import org.example.beexam.book.repository.AuthorRepository;
import org.example.beexam.book.repository.BookRepository;
import org.example.beexam.book.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<BookResponse> getAllBooks(int page, int size, String sortBy, String title) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Book> bookPage;

        if (title != null && !title.isBlank()) {
            bookPage = bookRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else {
            bookPage = bookRepository.findAll(pageable);
        }

        return bookPage.map(this::mapToResponse);
    }

    @Transactional
    public BookResponse createPrintedBook(PrintedBookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new RuntimeException("Un libro con questo ISBN esiste già");
        }

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Autore non trovato"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria non trovata"));

        PrintedBook book = new PrintedBook();
        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setPublishYear(request.getPublishYear());
        book.setTotalCopies(request.getTotalCopies());
        book.setAvailableCopies(request.getTotalCopies());
        book.setAuthor(author);
        book.setCategory(category);
        book.setPhysicalLocation(request.getPhysicalLocation());
        book.setPages(request.getPages());

        PrintedBook savedBook = bookRepository.save(book);
        return mapToResponse(savedBook);
    }

    @Transactional
    public BookResponse createEBook(EBookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new RuntimeException("Un libro con questo ISBN esiste già");
        }

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Autore non trovato"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria non trovata"));

        EBook book = new EBook();
        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setPublishYear(request.getPublishYear());
        book.setTotalCopies(request.getTotalCopies());
        book.setAvailableCopies(request.getTotalCopies());
        book.setAuthor(author);
        book.setCategory(category);
        book.setFileFormat(request.getFileFormat());
        book.setFileUrl(request.getFileUrl());

        EBook savedBook = bookRepository.save(book);
        return mapToResponse(savedBook);
    }

    @Transactional(readOnly = true)
    public List<CategoryBookCountResponse> getBooksCountByCategory() {
        return bookRepository.countBooksByCategory();
    }

    @Transactional(readOnly = true)
    public long getTotalBooksCount() {
        return bookRepository.count();
    }

    private BookResponse mapToResponse(Book book) {
        BookResponse.BookResponseBuilder builder = BookResponse.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .authorName(book.getAuthor().getName())
                .categoryName(book.getCategory().getName())
                .publishYear(book.getPublishYear())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies());

        if (book instanceof PrintedBook pb) {
            builder.bookType("PRINTED")
                    .physicalLocation(pb.getPhysicalLocation())
                    .pages(pb.getPages());
        } else if (book instanceof EBook eb) {
            builder.bookType("EBOOK")
                    .fileFormat(eb.getFileFormat())
                    .fileUrl(eb.getFileUrl());
        }

        return builder.build();
    }
}