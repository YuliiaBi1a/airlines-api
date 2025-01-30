package com.yuliia.airlines_api.register;

import com.yuliia.airlines_api.roles.RoleService;
import com.yuliia.airlines_api.users.User;
import com.yuliia.airlines_api.users.UserDtoRequest;
import com.yuliia.airlines_api.users.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserRegisterService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserRegisterService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public Map<String, String> registerUser(UserDtoRequest request) {

        Optional<User> optionalUser = userRepository.findByUsername(request.username());
        if(optionalUser.isPresent()){
            throw new RuntimeException("User with the email " + request.username() + " already exist");
        }

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(request.password());
        String passwordDecoded = new String(decodedBytes);

        System.out.println("<------------ " + passwordDecoded);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordEncoded = encoder.encode(passwordDecoded);

        User newUser = request.toEntity();
        newUser.setPassword(passwordEncoded);
        newUser.setRoles(roleService.assignDefaultRole());

        userRepository.save(newUser);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Success");

        return response;
    }
}
