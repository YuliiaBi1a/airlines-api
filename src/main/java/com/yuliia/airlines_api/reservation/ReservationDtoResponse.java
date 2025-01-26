package com.yuliia.airlines_api.reservation;

import com.yuliia.airlines_api.flights.Flight;
import com.yuliia.airlines_api.users.User;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationDtoResponse(
        Long id,
        int reservedSeats,
        LocalTime reservationTime,
        LocalDate reservationDate,
        ReservationStatus status,
        LocalTime lockExpirationTime,
        Flight flight,
        User user
) {
    public static ReservationDtoResponse fromEntity(Reservation reservation){
        return new ReservationDtoResponse(
                reservation.getId(),
                reservation.getReservedSeats(),
                reservation.getReservationTime(),
                reservation.getReservationDate(),
                reservation.getStatus(),
                reservation.getLockExpirationTime(),
                reservation.getFlight(),
                reservation.getUser()
        );
    }
}
