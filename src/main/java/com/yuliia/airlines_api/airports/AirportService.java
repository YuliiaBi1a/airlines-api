package com.yuliia.airlines_api.airports;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirportService {

    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public List<AirportDtoResponse> findAllAirports(){
        List<Airport> airportList = airportRepository.findAll();
        return airportList.stream().map(AirportDtoResponse::fromEntity).toList();
    }

    public AirportDtoResponse createAirport(AirportDtoRequest request){
        Optional<Airport> optionalAirport = airportRepository.findByCode(request.code());
        if(optionalAirport.isPresent()){
            throw new RuntimeException("Airport with the code " + request.code() + " already exist");
        }
        Airport newAirport = request.toEntity();
        Airport saveAirport = airportRepository.save(newAirport);

        return AirportDtoResponse.fromEntity(saveAirport);
    }

    public List<AirportDtoResponse> searchByNameOrCode(String name, String code) {
        List<Airport> playerList = airportRepository.findByNameOrCode(name, code);
        if (playerList.isEmpty()) {
            throw new RuntimeException("Airport not found");
        }
        return playerList.stream()
                .map(AirportDtoResponse::fromEntity).toList();
    }

    public AirportDtoResponse updateAirport(Long id, AirportDtoRequest request) {
        Airport existingAirport = airportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Airport with id" + id + " not found."));

        Optional<Airport> conflictingAirport = airportRepository.findByCode(request.code());
        if (conflictingAirport.isPresent() && !conflictingAirport.get().getId().equals(existingAirport.getId())) {
            throw new RuntimeException("An airport with code " + request.code() + " already exists.");
        }

        existingAirport.setName(request.name());
        existingAirport.setCity(request.city());
        existingAirport.setCountry(request.country());
        existingAirport.setCode(request.code());

        Airport updatedAirport = airportRepository.save(existingAirport);
        return AirportDtoResponse.fromEntity(updatedAirport);
    }

    @Transactional
    public void deleteAirportById(Long id) {
        if (!airportRepository.existsById(id)) {
            throw  new RuntimeException("Airport with id " + id + " not found.");
        }
        airportRepository.deleteById(id);
    }
}
