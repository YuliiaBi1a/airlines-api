package com.yuliia.airlines_api.flights;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api-endpoint}")
public class FlightController {

private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }
    @GetMapping("/public/flights")
    public ResponseEntity<List<FlightDtoResponse>> getFlightList() {
            List<FlightDtoResponse> flights = flightService.findAllFlights();
            return new ResponseEntity<>(flights, HttpStatus.OK);
        }
    @PostMapping("/private/flights")
    public ResponseEntity<FlightDtoResponse> saveNewFlight(@RequestBody FlightDtoRequest request){
        FlightDtoResponse newFlight = flightService.createFlight(request);
        return new ResponseEntity<>(newFlight, HttpStatus.CREATED);
    }
    @PutMapping("/private/flights/{id}")
    public ResponseEntity<FlightDtoResponse> putFlight(@PathVariable Long id, @RequestBody FlightDtoRequest request) {
        FlightDtoResponse updatedFlight = flightService.updateFlight(id, request);
        return new ResponseEntity<>(updatedFlight, HttpStatus.OK);
    }

    @DeleteMapping("/private/flights/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlightById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
