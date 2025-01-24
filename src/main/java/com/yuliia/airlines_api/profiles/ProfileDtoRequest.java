package com.yuliia.airlines_api.profiles;

import com.yuliia.airlines_api.users.User;
import org.springframework.web.multipart.MultipartFile;

public record ProfileDtoRequest(
        String name,
        String surname,
        MultipartFile file,
        String email,
        String phone,
        Long userId
) {

    public Profile toEntity(User user, String imgUrl){
        return new Profile(
                this.name,
                this.surname,
                imgUrl,
                this.email,
                this.phone,
                user
        );
    }
}
