package org.example.beexam.reservation.service;

import lombok.RequiredArgsConstructor;
import org.example.beexam.book.entity.Book;
import org.example.beexam.book.entity.PrintedBook;
import org.example.beexam.book.repository.BookRepository;
import org.example.beexam.reservation.dto.ReservationRequest;
import org.example.beexam.reservation.dto.ReservationResponse;
import org.example.beexam.reservation.entity.Reservation;
import org.example.beexam.reservation.entity.ReservationStatus;
import org.example.beexam.reservation.repository.ReservationRepository;
import org.example.beexam.user.entity.User;
import org.example.beexam.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReservationResponse createReservation(ReservationRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Libro non trovato"));

        if (!(book instanceof PrintedBook)) {
            throw new RuntimeException("Solo i libri cartacei possono essere prenotati");
        }

        if (book.getAvailableCopies() > 0) {
            throw new RuntimeException("Il libro ha ancora copie disponibili. Effettua un prestito normale.");
        }

        if (reservationRepository.existsByUserIdAndBookIdAndStatus(user.getId(), book.getId(), ReservationStatus.PENDING)) {
            throw new RuntimeException("Hai già una prenotazione in attesa per questo libro");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setReservationDate(LocalDate.now());
        reservation.setStatus(ReservationStatus.PENDING);

        Reservation savedReservation = reservationRepository.save(reservation);
        return mapToResponse(savedReservation);
    }

    private ReservationResponse mapToResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .userEmail(reservation.getUser().getEmail())
                .bookTitle(reservation.getBook().getTitle())
                .reservationDate(reservation.getReservationDate())
                .status(reservation.getStatus().name())
                .build();
    }
}