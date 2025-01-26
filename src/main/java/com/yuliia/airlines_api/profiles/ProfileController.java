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

    @GetMapping("/public/profiles/{id}")
    public ResponseEntity<ProfileDtoResponse> getProfileList(@PathVariable Long id) {
        ProfileDtoResponse profile = profileService.findProfileById(id);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }
    @PostMapping("/public/profiles")
    public ResponseEntity<ProfileDtoResponse> saveNewProfile(@ModelAttribute ProfileDtoRequest request){
        ProfileDtoResponse newProfile = profileService.createProfile(request);
        return new ResponseEntity<>(newProfile, HttpStatus.CREATED);
    }
   @PutMapping("/public/profiles/{id}")
    public ResponseEntity<ProfileDtoResponse> putProfile(@PathVariable Long id, @ModelAttribute ProfileDtoRequest request) {
        ProfileDtoResponse updatedProfile = profileService.updateProfile(id, request);
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }

    @DeleteMapping("/public/profiles/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long id) {
        profileService.deleteProfileById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
