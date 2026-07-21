package com.example.shopapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.dtos.UserLoginDTO;
import com.example.shopapp.models.User;
import com.example.shopapp.services.IUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return bindingResult.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .reduce((message1, message2) -> message1 + ", " + message2)
                        .map(errorMessage -> ResponseEntity.badRequest().body(errorMessage))
                        .orElse(ResponseEntity.badRequest().body("Unknown error"));
            }
            User user = userService.createUser(userDTO);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO userDTO) {
        try {
            String token = userService.login(userDTO.getPhoneNumber(), userDTO.getPassword());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
