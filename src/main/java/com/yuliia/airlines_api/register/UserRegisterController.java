package com.yuliia.airlines_api.register;

import com.yuliia.airlines_api.users.User;
import com.yuliia.airlines_api.users.UserDtoRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api-endpoint}")
public class UserRegisterController {

    private final UserRegisterService userRegisterService;

    public UserRegisterController(UserRegisterService userRegisterService) {
        this.userRegisterService = userRegisterService;
    }
    @GetMapping("/private/users")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userRegisterService.findAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/register/users")
    public ResponseEntity<Map<String,String>> registerNewUser(@RequestBody UserDtoRequest request){
        Map<String, String> response = userRegisterService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
