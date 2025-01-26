package com.yuliia.airlines_api.reservation;

import com.yuliia.airlines_api.flights.Flight;
import com.yuliia.airlines_api.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private int reservedSeats;
    private LocalTime reservationTime;
    private LocalDate reservationDate;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.PENDING;
    private LocalTime lockExpirationTime;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Reservation(int reservedSeats, Flight flight, User user) {
        this.reservedSeats = reservedSeats;
        this.reservationTime = LocalTime.now();
        this.reservationDate = LocalDate.now();
        this.lockExpirationTime = LocalTime.of(0, 15);
        this.flight = flight;
        this.user = user;
    }
}
