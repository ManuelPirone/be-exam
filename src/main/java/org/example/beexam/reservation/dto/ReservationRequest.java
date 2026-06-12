package org.example.beexam.reservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationRequest {
    @NotNull(message = "L'ID del libro è obbligatorio")
    private Long bookId;
}