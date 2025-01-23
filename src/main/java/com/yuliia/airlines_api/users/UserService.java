package com.yuliia.airlines_api.users;

import com.yuliia.airlines_api.airports.Airport;
import com.yuliia.airlines_api.airports.AirportDtoRequest;
import com.yuliia.airlines_api.airports.AirportDtoResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //Get all users
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
    //Post a new user
    public void registerUser(UserDtoRequest request){
        Optional<User> optionalUser = userRepository.findByUsername(request.username());
        if(optionalUser.isPresent()){
            throw new RuntimeException("User with the email " + request.username() + " already exist");
        }
        User newUser = request.toEntity();
        userRepository.save(newUser);
    }
}
