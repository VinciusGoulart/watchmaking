package com.example.watchmaking.controller;

import com.example.watchmaking.dto.user.UserCreateDto;
import com.example.watchmaking.dto.user.UserUpdatePasswordDto;
import com.example.watchmaking.dto.user.UserViewDto;
import com.example.watchmaking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserViewDto> findByEmail(@PathVariable String email) {
        UserViewDto userViewDto = new UserViewDto(userService.findUserByEmail(email));
        return ResponseEntity.ok(userViewDto);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UserUpdatePasswordDto newPassword) {
        userService.updatePasswordForAuthenticatedUser(newPassword);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity<String> deleteUserByEmail(@PathVariable String email) {
        userService.softDeleteUserByEmail(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
