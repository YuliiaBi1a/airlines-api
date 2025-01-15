package com.yuliia.airlines_api.flights;

import com.yuliia.airlines_api.airports.Airport;
import java.time.LocalDateTime;

public record FlightDtoResponse(
        Long id,
        Airport departureAirport,
        Airport arrivalAirport,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        int availableSeats,
        FlightStatus status,
        double price
) {
    public static FlightDtoResponse fromEntity(Flight flight){
        return  new FlightDtoResponse(
                flight.getId(),
                flight.getDepartureAirport(),
                flight.getArrivalAirport(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getAvailableSeats(),
                flight.getStatus(),
                flight.getPrice()
        );
    }
}
