package com.yuliia.airlines_api.flights;

import com.yuliia.airlines_api.airports.Airport;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "departure_airport_id")
    private Airport departure_airport;

    @ManyToOne
    @JoinColumn(name = "arrival_airport_id")
    private Airport arrival_airport;

    private LocalDateTime departure_time;
    private LocalDateTime arrival_time;
    private int available_seats;

    @Enumerated(EnumType.STRING)
    private FlightStatus status = FlightStatus.AVAILABLE;

    private double price;

    public Flight(Airport departure_airport, Airport arrival_airport, LocalDateTime departure_time, LocalDateTime arrival_time, int available_seats, FlightStatus status, double price) {
        this.departure_airport = departure_airport;
        this.arrival_airport = arrival_airport;
        this.departure_time = departure_time;
        this.arrival_time = arrival_time;
        this.available_seats = available_seats;
        this.status = status;
        this.price = price;
    }
}
