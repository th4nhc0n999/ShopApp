package com.example.shopapp.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.dtos.UserLoginDTO;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult)
    {
        try {
            if(bindingResult.hasErrors())
        {
            return bindingResult.getAllErrors().stream()
            .map(error -> error.getDefaultMessage())
            .reduce((message1, message2) -> message1 + ", " + message2)
            .map(errorMessage -> ResponseEntity.badRequest().body(errorMessage))
            .orElse(ResponseEntity.badRequest().body("Unknown error"));
        }
            return ResponseEntity.ok("Resgister user successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO userDTO)
    {
        return ResponseEntity.ok("Some token");
    }
}
