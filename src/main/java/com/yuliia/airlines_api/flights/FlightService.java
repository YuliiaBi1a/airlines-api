package com.yuliia.airlines_api.flights;

import com.yuliia.airlines_api.airports.Airport;
import com.yuliia.airlines_api.airports.AirportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final FlightCriteriaRepository flightCriteriaRepository;

    public FlightService(FlightRepository flightRepository, AirportRepository airportRepository, FlightCriteriaRepository flightCriteriaRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
        this.flightCriteriaRepository = flightCriteriaRepository;
    }

    public List<FlightDtoResponse> findAllFlights(){
        List<Flight> flightList = flightRepository.findAll();
        return flightList.stream().map(FlightDtoResponse::fromEntity).toList();
    }

    public FlightDtoResponse createFlight(FlightDtoRequest request){
        Airport departureAirport = airportRepository.findById(request.departureAirportId())
                .orElseThrow(() -> new RuntimeException("Airport with id " + request.departureAirportId()+ " not found"));

        Airport arrivalAirport= airportRepository.findById(request.arrivalAirportId())
                .orElseThrow(() -> new RuntimeException("Airport with id " + request.arrivalAirportId()+ " not found"));

        Flight newFlight = request.toEntity(departureAirport, arrivalAirport);
        Flight saveFlight = flightRepository.save(newFlight);
        return FlightDtoResponse.fromEntity(saveFlight);
    }

   public List<FlightDtoResponse> filterFlights(String departureAirportCode, String departureAirportName,
                                                String arrivalAirportCode, String arrivalAirportName,
                                                LocalDate departureDate, int requiredSeats) {
       List<Flight> searchFlights = flightCriteriaRepository.findFlights(departureAirportCode, departureAirportName,
               arrivalAirportCode, arrivalAirportName,
               departureDate, requiredSeats);
       return searchFlights.stream().map(FlightDtoResponse::fromEntity).toList();
   }

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

    public void deleteFlightById(Long id) {
        if (!flightRepository.existsById(id)) {
            throw  new RuntimeException("Flight with id " + id + " not found.");
        }
        flightRepository.deleteById(id);
    }
}
