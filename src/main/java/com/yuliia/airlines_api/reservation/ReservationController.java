package com.yuliia.airlines_api.reservation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api-endpoint}")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/public/reservations/history/{userId}")
    public ResponseEntity<?> getReservationList(@PathVariable Long userId) {
        List<ReservationDtoResponse> reservations = reservationService.findReservationsByUserId(userId);
        if (reservations.isEmpty()) {
            return new ResponseEntity<>("No reservations found", HttpStatus.OK);
        }
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @PostMapping("/public/reservations")
    public ResponseEntity<ReservationDtoResponse> saveNewReservation(@RequestBody ReservationDtoRequest request){
        ReservationDtoResponse newReservation = reservationService.createReservation(request);
        return new ResponseEntity<>(newReservation, HttpStatus.CREATED);
    }

    @PutMapping("/public/reservations/confirmed/{reservationId}")
    public ResponseEntity<ReservationDtoResponse> updateReservationStatusToConfirmed(@PathVariable Long reservationId) {
        ReservationDtoResponse updateReservation = reservationService.updateConfirmReservation(reservationId);
        return new ResponseEntity<>(updateReservation, HttpStatus.OK);
    }

    @PutMapping("/public/reservations/cancelled/{reservationId}")
    public ResponseEntity<String> updateReservationStatusToCancelled(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return new ResponseEntity<>("Reservation has been cancelled", HttpStatus.OK);
    }

    @DeleteMapping("/public/reservations/history/clean")
    public ResponseEntity<String> deleteCancelledAndOutdatedReservations() {
        reservationService.deleteCancelledAndOutdatedReservations();
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }

}
