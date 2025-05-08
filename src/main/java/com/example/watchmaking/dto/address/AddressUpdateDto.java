package com.example.watchmaking.dto.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class AddressUpdateDto {
    @Length(min = 5, max = 100)
    String street;
    @Length(min = 1, max = 20)
    String number;
    @Length(min = 5, max = 100)
    String neighborhood;
    @Length(min = 3, max = 100)
    String city;
    @Length(min = 5, max = 100)
    String state;
    @Length(min = 8, max = 20)
    String zipCode;
    String complement;
}
