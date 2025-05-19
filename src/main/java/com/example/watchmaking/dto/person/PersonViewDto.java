package com.example.watchmaking.dto.person;

import com.example.watchmaking.dto.address.AddressViewDto;
import com.example.watchmaking.entity.Person;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonViewDto {
    @Length(min = 3, max = 100)
    String name;
    @CPF
    String cpf;
    @Past
    LocalDate birthDate;
    @Length(min = 9, max = 100)
    String phone;
    @Valid
    AddressViewDto address;

    public PersonViewDto(Person person) {
        this.name = person.getName();
        this.cpf = person.getCpf();
        this.birthDate = person.getBirthDate();
        this.phone = person.getPhone();
        this.address = new AddressViewDto(person.getAddress());
    }
}
