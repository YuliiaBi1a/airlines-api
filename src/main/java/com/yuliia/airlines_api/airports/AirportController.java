package com.yuliia.airlines_api.airports;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api-endpoint}")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping("/public/airports")
    public ResponseEntity<List<AirportDtoResponse>> getPlayerList(@RequestParam(required = false) String name,
                                                                 @RequestParam(required = false) String code) {
        if (name == null && code== null) {
            List<AirportDtoResponse> airports = airportService.findAllAirports();
            return new ResponseEntity<>(airports, HttpStatus.OK);
        }
        List<AirportDtoResponse> searchAirports = airportService.searchByNameOrCode(name, code);
        return new ResponseEntity<>(searchAirports, HttpStatus.OK);
    }

    @PostMapping("/private/airports")
    public ResponseEntity<AirportDtoResponse> saveNewAirport(@RequestBody AirportDtoRequest request){
        AirportDtoResponse newAirport = airportService.createAirport(request);
        return new ResponseEntity<>(newAirport, HttpStatus.CREATED);
    }
    @PutMapping("/private/airports/{id}")
    public ResponseEntity<AirportDtoResponse> putAirport(@PathVariable Long id, @RequestBody AirportDtoRequest request) {
        AirportDtoResponse updatedAirport = airportService.updateAirport(id, request);
        return new ResponseEntity<>(updatedAirport, HttpStatus.OK);
    }

    @DeleteMapping("/private/airports/{id}")
    public ResponseEntity<String> deleteAirport(@PathVariable Long id) {
        airportService.deleteAirportById(id);
        return new ResponseEntity<>("Airport has been deleted successfully.", HttpStatus.NO_CONTENT);
    }
}
