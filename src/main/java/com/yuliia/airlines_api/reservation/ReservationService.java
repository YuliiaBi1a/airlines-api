package com.yuliia.airlines_api.reservation;

import com.yuliia.airlines_api.airports.Airport;
import com.yuliia.airlines_api.flights.Flight;
import com.yuliia.airlines_api.flights.FlightDtoRequest;
import com.yuliia.airlines_api.flights.FlightDtoResponse;
import com.yuliia.airlines_api.flights.FlightRepository;
import com.yuliia.airlines_api.users.User;
import com.yuliia.airlines_api.users.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        editAvailableSeats(request, flight);

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


    private static void editAvailableSeats(ReservationDtoRequest request, Flight flight) {
        int availableSeats = flight.getAvailableSeats();
        if(availableSeats >= request.reservedSeats()){
            int newNumberAvailableSeats = availableSeats - request.reservedSeats();
            flight.setAvailableSeats(newNumberAvailableSeats);
        } else {
            throw new RuntimeException("Not enough available seats");
        }
    }
}
