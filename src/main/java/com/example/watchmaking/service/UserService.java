package com.example.watchmaking.service;

import com.example.watchmaking.dto.address.AddressCreateDto;
import com.example.watchmaking.dto.person.PersonCreateDto;
import com.example.watchmaking.dto.user.UserCreateDto;
import com.example.watchmaking.dto.user.UserUpdatePasswordDto;
import com.example.watchmaking.entity.Address;
import com.example.watchmaking.entity.Person;
import com.example.watchmaking.entity.User;
import com.example.watchmaking.entity.UserType;
import com.example.watchmaking.repository.AddressRepository;
import com.example.watchmaking.repository.PersonRepository;
import com.example.watchmaking.repository.UserRepository;
import com.example.watchmaking.repository.UserTypeRepository;
import com.example.watchmaking.util.expcetions.NotFoundException;
import com.example.watchmaking.util.expcetions.ResourceExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;


    @Transactional
    public User createUser(UserCreateDto dto) {
        this.validate(dto);

        AddressCreateDto addressDto = dto.getPerson().getAddress();
        Address address = addressRepository.save(new Address(
                addressDto.getStreet(),
                addressDto.getNumber(),
                addressDto.getNeighborhood(),
                addressDto.getCity(),
                addressDto.getState(),
                addressDto.getZipCode(),
                addressDto.getComplement()));

        PersonCreateDto personDto = dto.getPerson();

        Person person = personRepository.save(new Person(
                personDto.getName(),
                personDto.getCpf(),
                personDto.getBirthDate(),
                personDto.getPhone(),
                address));

        UserType userType = userTypeRepository.findById(dto.getUserTypeUuid())
                .orElseThrow(() -> new NotFoundException("Usuario não encontrado"));

        User user = new User(
                dto.getEmail(),
                dto.getPassword(),
                userType,
                person);

        return userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario não encontrado"));
    }

    @Transactional
    public void softDeleteUserByEmail(String email) {
        this.findUserByEmail(email);

        userRepository.softDeleteByEmail(email);
    }

    @Transactional
    public void updatePasswordByEmail(String email, UserUpdatePasswordDto newPassword) {
        User user = this.findUserByEmail(email);

        // TODO: codificar a senha com bcrypt (ex: newPassword = passwordEncoder.encode(newPassword))

        User newUser = new User(
                user.getUuid(),
                user.getEmail(),
                newPassword.getNewPassword(),
                user.getUserType(),
                user.getPerson()
        );

        userRepository.save(newUser);
    }

    private void validate(UserCreateDto dto) {
        Map<String, String> error = new HashMap<>();
        if (userRepository.existsByEmail(dto.getEmail())) {
            error.put("email ", " já existente");
        }
        if (userRepository.existsUserByPerson_Cpf(dto.getPerson().getCpf())) {
            error.put("cpf ", " já existente");
        }

        if (!error.isEmpty()) {
            throw new ResourceExistsException(String.valueOf(error));
        }
    }

    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$";

}
