package org.example.beexam.reservation.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class ReservationResponse {
    private Long id;
    private String userEmail;
    private String bookTitle;
    private LocalDate reservationDate;
    private String status;
}