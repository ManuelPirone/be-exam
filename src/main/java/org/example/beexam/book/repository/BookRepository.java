package org.example.beexam.book.repository;

import org.example.beexam.book.dto.CategoryBookCountResponse;
import org.example.beexam.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);

    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT new org.example.beexam.book.dto.CategoryBookCountResponse(c.name, COUNT(b.id)) " +
            "FROM Book b JOIN b.category c GROUP BY c.name")
    List<CategoryBookCountResponse> countBooksByCategory();
}