package com.yuliia.airlines_api.profiles;

import com.yuliia.airlines_api.storage.FileStorageService;
import com.yuliia.airlines_api.users.User;
import com.yuliia.airlines_api.users.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Value("${default.image.url}")
    private String defaultImageUrl;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository, FileStorageService fileStorageService) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    // Find all profiles
    public List<ProfileDtoResponse> findAllProfiles() {
        List<Profile> profileList = profileRepository.findAll();
        return profileList.stream().map(ProfileDtoResponse::fromEntity).toList();
    }

    // Create a new profile
    public ProfileDtoResponse createProfile(ProfileDtoRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User with id " + request.userId() + " not found"));

                String imgUrl = defaultImageUrl;
        if (request.file() != null && !request.file().isEmpty()) {
            imgUrl = fileStorageService.imgUpload(request.file());
        }

        Profile newProfile = request.toEntity(user, imgUrl);
        Profile savedProfile = profileRepository.save(newProfile);
        return ProfileDtoResponse.fromEntity(savedProfile);
    }

    // Find profile by ID
    public ProfileDtoResponse findProfileById(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile with " + id + " not found"));
        return ProfileDtoResponse.fromEntity(profile);
    }

    // Update profile
    public ProfileDtoResponse updateProfile(Long id, ProfileDtoRequest request) {
        Profile existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile with id " + id + " not found."));

        String oldImgUrl = existingProfile.getImage();
        String newImgUrl = oldImgUrl;

        if (request.file() != null && !request.file().isEmpty()) {
            newImgUrl = fileStorageService.imgUpload(request.file());

            if (oldImgUrl != null && !oldImgUrl.equals(defaultImageUrl)) {
                fileStorageService.deleteImg(oldImgUrl);
            }
        }

        existingProfile.setName(request.name());
        existingProfile.setSurname(request.surname());
        existingProfile.setEmail(request.email());
        existingProfile.setImage(newImgUrl);
        existingProfile.setPhone(request.phone());

        Profile updatedProfile = profileRepository.save(existingProfile);
        return ProfileDtoResponse.fromEntity(updatedProfile);
    }


    // Delete profile
    public void deleteProfileById(Long id) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isEmpty()) {
            throw new RuntimeException("Profile with id " + id + " not found.");
        }
        String imgUrlToDelete = optionalProfile.get().getImage();

        if (!defaultImageUrl.equals(imgUrlToDelete)) {
            fileStorageService.deleteImg(imgUrlToDelete);
        }

        profileRepository.deleteById(id);
    }
}
