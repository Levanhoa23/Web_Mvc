package com.example.levanhoa.controller.api;

import com.example.levanhoa.dto.ApiResponse;
import com.example.levanhoa.dto.RegisterRequest;
import com.example.levanhoa.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class ApiAuthController {
    @PostMapping("Website/register")
    public ApiResponse register(@RequestBody RegisterRequest registerRequest){
        if(registerRequest.getEmail().equals("admin@gmail.com")){
            return new ApiResponse<>(500, "Email da ton tai", null);
        }
        return new ApiResponse<>(200, "Register success", null);
    }

    @PostMapping("Website/login")
    public ResponseEntity<User> login(@RequestParam("email") String email, @RequestParam("password") String password){
        if(email.equals("admin@gmail.com") && password.equals("123")){
            return new ResponseEntity<>(new User(email, password, "Admin"), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
