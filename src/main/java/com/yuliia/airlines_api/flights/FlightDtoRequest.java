package com.yuliia.airlines_api.flights;

import com.yuliia.airlines_api.airports.Airport;
import java.time.LocalDateTime;

public record FlightDtoRequest(
        Long departureAirportId,
        Long arrivalAirportId,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        int availableSeats,
        FlightStatus status,
        double price
) {
    public Flight toEntity(Airport departureAirport, Airport arrivalAirport){
        return new Flight(
                departureAirport,
                arrivalAirport,
                this.departureTime,
                this.arrivalTime,
                this.availableSeats,
                this.status,
                this.price
        );
    }
}
