package com.yuliia.airlines_api.reservation;

import com.yuliia.airlines_api.profiles.ProfileDtoRequest;
import com.yuliia.airlines_api.profiles.ProfileDtoResponse;
import com.yuliia.airlines_api.profiles.ProfileService;
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

    @GetMapping("/public/reservations/{userId}")
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


}
