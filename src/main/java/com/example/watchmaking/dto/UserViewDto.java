package com.example.watchmaking.dto;

import com.example.watchmaking.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserViewDto {
    UUID uuid;
    String email;
    String password;
    UUID userTypeUuid;
    UUID personUuid;

    public UserViewDto(User user) {
        this.uuid = user.getUuid();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.userTypeUuid = user.getUserType().getUuid();
        this.personUuid = user.getPerson().getUuid();
    }
}
