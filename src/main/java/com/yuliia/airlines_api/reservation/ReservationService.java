package com.yuliia.airlines_api.reservation;

import com.yuliia.airlines_api.flights.Flight;
import com.yuliia.airlines_api.flights.FlightRepository;
import com.yuliia.airlines_api.flights.FlightStatus;
import com.yuliia.airlines_api.users.User;
import com.yuliia.airlines_api.users.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, FlightRepository flightRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.flightRepository = flightRepository;
    }

    //Post a new reservation
    public ReservationDtoResponse createReservation(ReservationDtoRequest request){
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User with id " + request.userId()+ " not found"));

        Flight flight = flightRepository.findById(request.flightId())
                .orElseThrow(() -> new RuntimeException("Flight with id " + request.flightId()+ " not found"));

        if (LocalDateTime.now().isAfter(flight.getDepartureTime())) {
            throw new RuntimeException("Reservations can only be realized before the flight departure.");
        }
        reserveAvailableSeats(request, flight);

        Reservation newReservation = request.toEntity(flight, user);
        Reservation saveReservation = reservationRepository.save(newReservation);
        return ReservationDtoResponse.fromEntity(newReservation);
    }

    //Find all reservations by User ID
    public List<ReservationDtoResponse> findReservationsByUserId(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId+ " not found"));

        List<Reservation> reservationList = reservationRepository.findByUserId(userId);
        return reservationList.stream().map(ReservationDtoResponse::fromEntity).toList();
    }

    //Confirm reservation
    public ReservationDtoResponse updateConfirmReservation(Long id) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation with id " + id + " not found."));

        Flight flight = existingReservation.getFlight();
        LocalTime currentTime = LocalTime.now();
        LocalTime reservationTime = existingReservation.getReservationTime();
        LocalTime lockExpirationTime = reservationTime.plusMinutes(2);

        if (currentTime.isBefore(lockExpirationTime) && lockExpirationTime.isAfter(reservationTime)) {
            existingReservation.setStatus(ReservationStatus.CONFIRMED);
        } else {
            restoreAvailableSeats(existingReservation, flight);
            reservationRepository.deleteById(id);
            throw new RuntimeException("The lock expiration time has passed, please start new reservation.");
        }
       Reservation updateReservation =  reservationRepository.save(existingReservation);
        return ReservationDtoResponse.fromEntity(updateReservation);
    }
    //cancelReservation - cambiamos estado a cancelado
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation with id " + reservationId + " not found."));

        Flight flight = reservation.getFlight();

        if (reservation.getStatus().equals(ReservationStatus.CANCELLED)) {
            throw new RuntimeException("Reservation is already cancelled.");
        }
        if (flight.getDepartureTime().isBefore(LocalDateTime.now().plusHours(24))) {
            throw new RuntimeException("Reservations can only be cancelled at least 24 hours before the flight departure.");
        }
        restoreAvailableSeats(reservation, flight);
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }
    //Clean history reservation: delete canceled and outdated reservations
    public void deleteCancelledAndOutdatedReservations() {
        List<Reservation> cancelledReservations = reservationRepository.findByStatus(ReservationStatus.CANCELLED);
        List<Reservation> outdatedReservations = reservationRepository.findByStatus(ReservationStatus.OUTDATED);

        Set<Reservation> reservationsToDelete = new HashSet<>();
        reservationsToDelete.addAll(cancelledReservations);
        reservationsToDelete.addAll(outdatedReservations);

        if (reservationsToDelete.isEmpty()) {
            throw new RuntimeException("No cancelled or outdated reservations found.");
        }
        reservationRepository.deleteAll(reservationsToDelete);
    }

// Automáticamente cambiamos estado de reserva a outdated si vuelo ya esta realizado
// Testeado con Postman ++
public void updateOutdatedReservations() {
    List<Reservation> confirmedReservations = reservationRepository.findConfirmedAndOutdatedReservations(LocalDateTime.now());
    confirmedReservations.forEach(reservation -> reservation.setStatus(ReservationStatus.OUTDATED));
    reservationRepository.saveAll(confirmedReservations);
}

//---------------------------------------------------------------------//
//Comprobamos si hay asientos disponibles, si has reservado últimos asientos - cambiamos estado de vuelo a FULL
    private static void reserveAvailableSeats(ReservationDtoRequest request, Flight flight) {
        int availableSeats = flight.getAvailableSeats();
        if(availableSeats >= request.reservedSeats()){
            int newNumberAvailableSeats = availableSeats - request.reservedSeats();
            flight.setAvailableSeats(newNumberAvailableSeats);

            if (newNumberAvailableSeats == 0) {
                flight.setStatus(FlightStatus.FULL);
            }
        } else {
            throw new RuntimeException("Not enough available seats");
        }
    }
// Restaurar asientos si cancelamos reserva, cambiamos estado de vuelo si es necesario a AVAILABLE
    private static void restoreAvailableSeats(Reservation reservation, Flight flight) {
        int availableSeats = flight.getAvailableSeats();
        int reservedSeats = reservation.getReservedSeats();
        int newNumberAvailableSeats = availableSeats + reservedSeats;
        flight.setAvailableSeats(newNumberAvailableSeats);
        if (newNumberAvailableSeats > 0 && flight.getStatus() == FlightStatus.FULL) {
            flight.setStatus(FlightStatus.AVAILABLE);
        }
    }
//TODO método que automáticamente restaura números de asientos si después de 15 minutos estado de reserva sigue PENDING y borra la reserva
}
