package com.example.watchmaking.controller;

import com.example.watchmaking.dto.UserCreateDto;
import com.example.watchmaking.dto.UserViewDto;
import com.example.watchmaking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        userService.createUser(userCreateDto);
        return ResponseEntity.ok("User created successfully");
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserViewDto> findByEmail(@PathVariable String email) {
        UserViewDto userViewDto = new UserViewDto(userService.findUserByEmail(email));
        return ResponseEntity.ok(userViewDto);
    }
}
