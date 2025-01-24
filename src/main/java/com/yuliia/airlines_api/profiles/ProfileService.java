package com.yuliia.airlines_api.profiles;

import com.yuliia.airlines_api.airports.Airport;
import com.yuliia.airlines_api.flights.Flight;
import com.yuliia.airlines_api.flights.FlightDtoRequest;
import com.yuliia.airlines_api.flights.FlightDtoResponse;
import com.yuliia.airlines_api.storage.FileStorageService;
import com.yuliia.airlines_api.users.User;
import com.yuliia.airlines_api.users.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;


    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository, FileStorageService fileStorageService) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    //Find all profile
    public List<ProfileDtoResponse> findAllProfiles(){
        List<Profile> profileList = profileRepository.findAll();
        return profileList.stream().map(ProfileDtoResponse::fromEntity).toList();
    }

    //Create a new profile
    public ProfileDtoResponse createProfile(ProfileDtoRequest request){
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User with id " + request.userId() + " not found"));
        String imgUrl = null;
        if (request.file() != null && !request.file().isEmpty()) {
            imgUrl = fileStorageService.imgUpload(request.file());
        }

        Profile newProfile = request.toEntity(user, imgUrl);
        Profile saveProfile = profileRepository.save(newProfile);
        return ProfileDtoResponse.fromEntity(saveProfile);
    }

    // Find profile by ID
    public ProfileDtoResponse findProfileById(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile with " + id + " not found"));
        return ProfileDtoResponse.fromEntity(profile);
    }

/*    // Update an airport
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
    }*/

}
