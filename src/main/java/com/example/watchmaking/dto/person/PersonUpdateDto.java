package com.example.watchmaking.dto.person;

import com.example.watchmaking.dto.address.AddressUpdateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonUpdateDto {
    @Length(min = 3, max = 100)
    String name;
    @CPF
    String cpf;
    @Past
    LocalDate birthDate;
    @Length(min = 9, max = 100)
    String phone;
    @Valid
    AddressUpdateDto address;

}
