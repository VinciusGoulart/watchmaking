package com.example.watchmaking.dto;


import com.example.watchmaking.entity.Address;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class AddressCreateDto {
    @NotBlank @Length(min = 5, max = 100)
    String street;
    @NotBlank @Length(min = 1, max = 20)
    String number;
    @NotBlank @Length(min = 5, max = 100)
    String neighborhood;
    @NotBlank @Length(min = 3, max = 100)
    String city;
    @NotBlank @Length(min = 5, max = 100)
    String state;
    @NotBlank @Length(min = 8, max = 20)
    String zipCode;
    String complement;

}
