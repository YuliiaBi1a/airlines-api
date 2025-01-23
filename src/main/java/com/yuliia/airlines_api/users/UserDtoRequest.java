package com.yuliia.airlines_api.users;

public record UserDtoRequest(String username, String password) {
    public User toEntity(){
        return new User(this.username, this.password);
    }
}
