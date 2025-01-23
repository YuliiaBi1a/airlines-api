package com.yuliia.airlines_api.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api-endpoint}")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/private/users")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/register/user")
    public ResponseEntity<?> registerNewUser(@RequestBody UserDtoRequest request){
        userService.registerUser(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
