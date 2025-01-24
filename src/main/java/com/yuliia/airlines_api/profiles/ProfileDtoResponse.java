package com.yuliia.airlines_api.profiles;

import com.yuliia.airlines_api.users.User;

public record ProfileDtoResponse(Long Id,
                                 String name,
                                 String surname,
                                 String image,
                                 String email,
                                 String phone,
                                 User user
) {
    public static ProfileDtoResponse fromEntity(Profile profile){
        return new ProfileDtoResponse(
                profile.getId(),
                profile.getName(),
                profile.getSurname(),
                profile.getImage(),
                profile.getEmail(),
                profile.getPhone(),
                profile.getUser()
        );
    }
}
