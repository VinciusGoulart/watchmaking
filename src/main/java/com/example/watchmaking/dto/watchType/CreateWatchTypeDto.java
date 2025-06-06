package com.example.watchmaking.dto.watchType;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWatchTypeDto {
    @NotBlank
    @Length(min=2, max=100)
    private String name;

    @NotBlank
    @Length(min=2, max=50)
    private String code;
}
