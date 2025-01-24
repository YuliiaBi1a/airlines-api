package com.yuliia.airlines_api.profiles;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api-endpoint}")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/private/profiles")
    public ResponseEntity<List<ProfileDtoResponse>> getProfileList() {
        List<ProfileDtoResponse> profiles = profileService.findAllProfiles();
        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }
    @PostMapping("/public/profiles")
    public ResponseEntity<ProfileDtoResponse> saveNewProfile(@ModelAttribute ProfileDtoRequest request){
        ProfileDtoResponse newProfile = profileService.createProfile(request);
        return new ResponseEntity<>(newProfile, HttpStatus.CREATED);
    }
   /* @PutMapping("/private/flights/{id}")
    public ResponseEntity<FlightDtoResponse> putFlight(@PathVariable Long id, @RequestBody FlightDtoRequest request) {
        FlightDtoResponse updatedFlight = flightService.updateFlight(id, request);
        return new ResponseEntity<>(updatedFlight, HttpStatus.OK);
    }

    @DeleteMapping("/private/flights/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlightById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }*/
}
