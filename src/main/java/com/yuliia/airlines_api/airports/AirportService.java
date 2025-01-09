package com.yuliia.airlines_api.airports;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AirportService {

    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    //Post new airport
    public AirportDtoResponse createAirport(AirportDtoRequest request){
        Optional<Airport> optionalAirport = airportRepository.findByCode(request.code());
        if(optionalAirport.isPresent()){
            throw new RuntimeException("Airport with the code " + request.code() + " already exist");
        }
        Airport newAirport = request.toEntity();
        Airport saveAirport = airportRepository.save(newAirport);

        return AirportDtoResponse.fromEntity(saveAirport);
    }
}
