package org.example.beexam.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.example.beexam.reservation.dto.ReservationRequest;
import org.example.beexam.reservation.dto.ReservationResponse;
import org.example.beexam.reservation.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Validated @RequestBody ReservationRequest request,
            Principal principal
    ) {
        ReservationResponse response = reservationService.createReservation(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}