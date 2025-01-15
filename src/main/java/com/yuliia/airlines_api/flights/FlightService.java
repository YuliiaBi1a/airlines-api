package com.yuliia.airlines_api.flights;

import com.yuliia.airlines_api.airports.Airport;
import com.yuliia.airlines_api.airports.AirportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;

    public FlightService(FlightRepository flightRepository, AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
    }
    //Find all flights
    public List<FlightDtoResponse> findAllFlights(){
        List<Flight> flightList = flightRepository.findAll();
        return flightList.stream().map(FlightDtoResponse::fromEntity).toList();
    }

    //Post a new flight
    public FlightDtoResponse createFlight(FlightDtoRequest request){
        Airport departureAirport = airportRepository.findById(request.departureAirportId())
                .orElseThrow(() -> new RuntimeException("Airport with id " + request.departureAirportId()+ " not found"));

        Airport arrivalAirport= airportRepository.findById(request.arrivalAirportId())
                .orElseThrow(() -> new RuntimeException("Airport with id " + request.arrivalAirportId()+ " not found"));

        Flight newFlight = request.toEntity(departureAirport, arrivalAirport);
        Flight saveFlight = flightRepository.save(newFlight);
        return FlightDtoResponse.fromEntity(saveFlight);
    }
/*    // Search an airport like name or code
    public List<AirportDtoResponse> searchByNameOrCode(String name, String code) {
        List<Airport> playerList = airportRepository.findByNameOrCode(name, code);
        if (playerList.isEmpty()) {
            throw new RuntimeException("Airport not found");
        }
        return playerList.stream()
                .map(AirportDtoResponse::fromEntity).toList();
    }*/
    // Update an airport
    public FlightDtoResponse updateFlight(Long id, FlightDtoRequest request) {
        Flight existingFlight = flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight with id" + id + " not found."));

        Airport departureAirport = airportRepository.findById(request.departureAirportId())
                .orElseThrow(() -> new RuntimeException("Airport with id " + request.departureAirportId()+ " not found"));

        Airport arrivalAirport= airportRepository.findById(request.arrivalAirportId())
                .orElseThrow(() -> new RuntimeException("Airport with id " + request.arrivalAirportId()+ " not found"));

        existingFlight.setDepartureAirport(departureAirport);
        existingFlight.setArrivalAirport(arrivalAirport);
        existingFlight.setDepartureTime(request.departureTime());
        existingFlight.setArrivalTime(request.arrivalTime());
        existingFlight.setAvailableSeats(request.availableSeats());
        existingFlight.setStatus(request.status());
        existingFlight.setPrice(request.price());

        Flight updatedFlight = flightRepository.save(existingFlight);
        return FlightDtoResponse.fromEntity(updatedFlight);
    }

    // Delete a flight
    public void deleteFlightById(Long id) {
        if (!flightRepository.existsById(id)) {
            throw  new RuntimeException("Flight with id " + id + " not found.");
        }
        flightRepository.deleteById(id);
    }

}
