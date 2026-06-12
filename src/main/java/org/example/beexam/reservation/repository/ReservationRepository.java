package org.example.beexam.reservation.repository;

import org.example.beexam.reservation.entity.Reservation;
import org.example.beexam.reservation.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByUserIdAndBookIdAndStatus(Long userId, Long bookId, ReservationStatus status);
}