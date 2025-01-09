package com.yuliia.airlines_api.airports;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @PostMapping("/private/airports")
    public ResponseEntity<AirportDtoResponse> saveNewAirport(@RequestBody AirportDtoRequest request){
        AirportDtoResponse newAirport = airportService.createAirport(request);
        return new ResponseEntity<>(newAirport, HttpStatus.CREATED);
    }
}
