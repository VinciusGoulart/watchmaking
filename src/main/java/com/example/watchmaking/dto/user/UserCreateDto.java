package com.example.watchmaking.dto.user;

import com.example.watchmaking.dto.person.PersonCreateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserCreateDto {
    @Email @Length(min = 8, max = 255) @NotBlank
    String email;
    @NotBlank
    @Length(min = 8, max = 255)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$", message = "A senha deve conter no mínimo 8" +
            " caracteres, incluindo letra maiúscula, letra minúscula e um caractere especial.")
    String password;
    @NotNull
    UUID userTypeUuid;
    @NotNull
    @Valid
    PersonCreateDto person;
}
