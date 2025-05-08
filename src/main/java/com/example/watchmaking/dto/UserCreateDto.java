package com.example.watchmaking.dto;

import com.example.watchmaking.entity.Person;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserCreateDto {
    @Email @Length(min = 8, max = 255) @NotBlank
    String email;
    @NotBlank @Length(min = 6, max = 255)
    String password;
    @NotNull
    UUID userTypeUuid;
    @NotNull
    @Valid
    PersonCreateDto person;
}
