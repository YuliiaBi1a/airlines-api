package com.yuliia.airlines_api.reservation;

import com.yuliia.airlines_api.flights.Flight;
import com.yuliia.airlines_api.users.User;

public record ReservationDtoRequest(
        int reservedSeats,
        Long flightId,
        Long userId
) {
    public Reservation toEntity(Flight flight, User user){
        return new Reservation(
                this.reservedSeats,
                flight,
                user
        );
    }
}
