package org.example.beexam.loan.service;

import lombok.RequiredArgsConstructor;
import org.example.beexam.book.entity.Book;
import org.example.beexam.book.entity.PrintedBook;
import org.example.beexam.book.repository.BookRepository;
import org.example.beexam.loan.dto.LoanRequest;
import org.example.beexam.loan.dto.LoanResponse;
import org.example.beexam.loan.entity.Loan;
import org.example.beexam.loan.entity.LoanStatus;
import org.example.beexam.loan.repository.LoanRepository;
import org.example.beexam.user.entity.User;
import org.example.beexam.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    public LoanResponse borrowBook(LoanRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Libro non trovato"));

        if (!(book instanceof PrintedBook)) {
            throw new RuntimeException("Solo i libri cartacei possono essere presi in prestito fisicamente");
        }

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("Nessuna copia disponibile per questo libro al momento");
        }

        if (loanRepository.existsByUserIdAndBookIdAndReturnDateIsNull(user.getId(), book.getId())) {
            throw new RuntimeException("Hai già una copia di questo libro in prestito");
        }

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(30)); // Scadenza a 30 giorni
        loan.setStatus(LoanStatus.ACTIVE);

        book.setAvailableCopies(book.getAvailableCopies() - 1);

        Loan savedLoan = loanRepository.save(loan);
        return mapToResponse(savedLoan);
    }

    @Transactional
    public LoanResponse returnBook(Long loanId, String userEmail) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Prestito non trovato"));

        if (!loan.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Non sei autorizzato a restituire un prestito di un altro utente");
        }

        if (loan.getReturnDate() != null) {
            throw new RuntimeException("Questo libro è già stato restituito in data " + loan.getReturnDate());
        }
        loan.setReturnDate(LocalDate.now());
        loan.setStatus(LoanStatus.RETURNED);

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        Loan savedLoan = loanRepository.save(loan);
        return mapToResponse(savedLoan);
    }

    private LoanResponse mapToResponse(Loan loan) {
        return LoanResponse.builder()
                .id(loan.getId())
                .userEmail(loan.getUser().getEmail())
                .bookTitle(loan.getBook().getTitle())
                .loanDate(loan.getLoanDate())
                .dueDate(loan.getDueDate())
                .returnDate(loan.getReturnDate())
                .status(loan.getStatus().name())
                .build();
    }
}