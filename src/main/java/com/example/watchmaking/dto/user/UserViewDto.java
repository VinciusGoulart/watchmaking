package com.example.watchmaking.dto.user;

import com.example.watchmaking.dto.person.PersonViewDto;
import com.example.watchmaking.entity.Person;
import com.example.watchmaking.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserViewDto {
    UUID uuid;
    String email;
    String userType;
    Boolean isDeleted;
    PersonViewDto person;

    public UserViewDto(User user) {
        this.uuid = user.getUuid();
        this.email = user.getEmail();
        this.userType = user.getUserType().getName();
        this.isDeleted = user.getIsDeleted();
        this.person = new PersonViewDto(user.getPerson());
    }
}
