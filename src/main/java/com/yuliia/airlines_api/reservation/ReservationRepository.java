package com.yuliia.airlines_api.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId")
    List<Reservation> findByUserId(@Param("userId") Long userId);

    List<Reservation> findByStatus(ReservationStatus reservationStatus);

    @Query("SELECT r FROM Reservation r WHERE r.status = 'CONFIRMED' AND r.flight.departureTime < :now")
    List<Reservation> findConfirmedAndOutdatedReservations(@Param("now") LocalDateTime now);
}
