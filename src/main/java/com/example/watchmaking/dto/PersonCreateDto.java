package com.example.watchmaking.dto;

import com.example.watchmaking.entity.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;


public class PersonCreateDto {
    @NotBlank
    @Length(min = 3, max = 100)
    String name;
    @CPF @NotBlank
    String cpf;
    @Past @NotNull
    LocalDate birthDate;
    @NotBlank
    @Length(min = 9, max = 100)
    String phone;
    @NotNull
    @Valid
    AddressCreateDto address;

    public PersonCreateDto(String name, String cpf, LocalDate birthDate, String phone, AddressCreateDto address) {
        this.name = name;
        this.cpf = cpf;
        this.birthDate = birthDate;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AddressCreateDto getAddress() {
        return address;
    }

    public void setAddress(AddressCreateDto address) {
        this.address = address;
    }
}
