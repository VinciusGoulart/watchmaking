package com.example.watchmaking.service;

import com.example.watchmaking.dto.address.AddressCreateDto;
import com.example.watchmaking.dto.person.PersonCreateDto;
import com.example.watchmaking.dto.user.UserCreateDto;
import com.example.watchmaking.dto.user.UserUpdatePasswordDto;
import com.example.watchmaking.dto.user.UserViewDto;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private PasswordEncoder passwordEncoder;


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
                .orElseThrow(() -> new NotFoundException("Tipo de Usuário não encontrado"));


        String password = dto.getPassword();
        String encryptedPassword = new BCryptPasswordEncoder().encode(password);

        User user = new User(
                dto.getEmail(),
                encryptedPassword,
                userType,
                person);

        return userRepository.save(user);
    }

    public UserViewDto findUserByEmail(String email) {
        User user=  userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        return new UserViewDto(user);
    }

    @Transactional
    public void softDeleteUserByEmail(String email) {
        this.findUserByEmail(email);

        userRepository.softDeleteByEmail(email);
    }

    @Transactional
    public void updatePasswordForAuthenticatedUser(UserUpdatePasswordDto passwordDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Senha atual incorreta");
        }

        String encodedNewPassword = passwordEncoder.encode(passwordDto.getNewPassword());

        User newUser = new User(
                user.getUuid(),
                user.getEmail(),
                encodedNewPassword,
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
