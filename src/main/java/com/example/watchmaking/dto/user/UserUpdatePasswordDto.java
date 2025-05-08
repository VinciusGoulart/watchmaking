package com.example.watchmaking.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class UserUpdatePasswordDto {
    @NotBlank
    @Length(min = 8, max = 255)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$", message = "A senha deve conter no mínimo 8" +
            " caracteres, incluindo letra maiúscula, letra minúscula e um caractere especial.")
    String newPassword;
}
