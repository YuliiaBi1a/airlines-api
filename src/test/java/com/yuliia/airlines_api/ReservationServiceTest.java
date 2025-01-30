package com.yuliia.airlines_api;

import com.yuliia.airlines_api.flights.Flight;
import com.yuliia.airlines_api.flights.FlightRepository;
import com.yuliia.airlines_api.reservation.*;
import com.yuliia.airlines_api.users.User;
import com.yuliia.airlines_api.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_createReservationSuccessfully() {
        // GIVEN
        ReservationDtoRequest request = new ReservationDtoRequest(2, 1L, 1L);
        User user = new User();
        user.setId(1L);

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeats(10);
        flight.setDepartureTime(LocalDateTime.now().plusHours(2));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
            Reservation reservation = invocation.getArgument(0);
            reservation.setId(1L);
            return reservation;
        });

        // WHEN
        ReservationDtoResponse response = reservationService.createReservation(request);

        // THEN
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(2, response.reservedSeats());
        assertEquals(ReservationStatus.PENDING, response.status());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void should_throwExceptionWhenUserNotFound() {
        // GIVEN
        ReservationDtoRequest request = new ReservationDtoRequest(2, 1L, 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> reservationService.createReservation(request));
        assertEquals("User with id 1 not found", exception.getMessage());
    }

    @Test
    void should_throwExceptionWhenFlightNotFound() {
        // GIVEN
        ReservationDtoRequest request = new ReservationDtoRequest(2, 1L, 1L);
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> reservationService.createReservation(request));
        assertEquals("Flight with id 1 not found", exception.getMessage());
    }

    @Test
    void should_throwExceptionWhenNotEnoughSeats() {
        // GIVEN
        ReservationDtoRequest request = new ReservationDtoRequest(5, 1L, 1L);
        User user = new User();
        user.setId(1L);

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeats(3);
        flight.setDepartureTime(LocalDateTime.now().plusHours(2));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> reservationService.createReservation(request));
        assertEquals("Not enough available seats", exception.getMessage());
    }

    @Test
    void should_confirmReservationSuccessfully() {
        // GIVEN
        Reservation reservation = new Reservation(2, new Flight(), new User());
        reservation.setId(1L);
        reservation.setReservationTime(LocalTime.now().minusMinutes(1)); // Within the 2-minute window

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // WHEN
        ReservationDtoResponse response = reservationService.updateConfirmReservation(1L);

        // THEN
        assertNotNull(response);
        assertEquals(ReservationStatus.CONFIRMED, response.status());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void should_throwExceptionWhenConfirmingExpiredReservation() {
        // GIVEN
        Reservation reservation = new Reservation(2, new Flight(), new User());
        reservation.setId(1L);
        reservation.setReservationTime(LocalTime.now().minusMinutes(3)); // Outside the 2-minute window

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> reservationService.updateConfirmReservation(1L));
        assertEquals("The lock expiration time has passed, please start new reservation.", exception.getMessage());
        verify(reservationRepository).deleteById(1L);
    }

    @Test
    void should_cancelReservationSuccessfully() {
        // GIVEN
        Reservation reservation = new Reservation(2, new Flight(), new User());
        reservation.setId(1L);
        reservation.setStatus(ReservationStatus.PENDING);

        Flight flight = new Flight();
        flight.setAvailableSeats(5);
        flight.setDepartureTime(LocalDateTime.now().plusHours(25)); // More than 24 hours before departure
        reservation.setFlight(flight);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // WHEN
        reservationService.cancelReservation(1L);

        // THEN
        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void should_throwExceptionWhenCancellingAlreadyCancelledReservation() {
        // GIVEN
        Reservation reservation = new Reservation(2, new Flight(), new User());
        reservation.setId(1L);
        reservation.setStatus(ReservationStatus.CANCELLED);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> reservationService.cancelReservation(1L));
        assertEquals("Reservation is already cancelled.", exception.getMessage());
    }

    @Test
    void should_throwExceptionWhenCancellingReservationLessThan24HoursBeforeDeparture() {
        // GIVEN
        Reservation reservation = new Reservation(2, new Flight(), new User());
        reservation.setId(1L);
        reservation.setStatus(ReservationStatus.PENDING);

        Flight flight = new Flight();
        flight.setDepartureTime(LocalDateTime.now().plusHours(23)); // Less than 24 hours before departure
        reservation.setFlight(flight);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> reservationService.cancelReservation(1L));
        assertEquals("Reservations can only be cancelled at least 24 hours before the flight departure.", exception.getMessage());
    }

    @Test
    void should_deleteCancelledAndOutdatedReservationsSuccessfully() {
        // GIVEN
        Reservation cancelledReservation = new Reservation(2, new Flight(), new User());
        cancelledReservation.setId(1L);
        cancelledReservation.setStatus(ReservationStatus.CANCELLED);

        Reservation outdatedReservation = new Reservation(3, new Flight(), new User());
        outdatedReservation.setId(2L);
        outdatedReservation.setStatus(ReservationStatus.OUTDATED);

        when(reservationRepository.findByStatusAndUserId(ReservationStatus.CANCELLED, 1L)).thenReturn(List.of(cancelledReservation));
        when(reservationRepository.findByStatusAndUserId(ReservationStatus.OUTDATED, 1L)).thenReturn(List.of(outdatedReservation));

        // WHEN
        reservationService.deleteCancelledAndOutdatedReservations(1L);

        // THEN
        verify(reservationRepository).deleteAll(anySet());
    }

    @Test
    void should_throwExceptionWhenNoCancelledOrOutdatedReservationsFound() {
        // GIVEN
        when(reservationRepository.findByStatusAndUserId(ReservationStatus.CANCELLED, 1L)).thenReturn(List.of());
        when(reservationRepository.findByStatusAndUserId(ReservationStatus.OUTDATED, 1L)).thenReturn(List.of());

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> reservationService.deleteCancelledAndOutdatedReservations(1L));
        assertEquals("No cancelled or outdated reservations found for user with ID: 1", exception.getMessage());
    }
}
