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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserTypeRepository userTypeRepository;

    @InjectMocks
    private UserService userService;

    private UserCreateDto userCreateDto;
    private Address address;
    private Person person;
    private User user;
    private UserType userType;
    private UUID userTypeUuid;

    @BeforeEach
    void setUp() {
        userTypeUuid = UUID.randomUUID();

        AddressCreateDto addressCreateDto = new AddressCreateDto(
                "Rua Teste",
                "123",
                "Bairro Teste",
                "Cidade Teste",
                "Estado Teste",
                "12345-678",
                "Complemento Teste"
        );

        PersonCreateDto personCreateDto = new PersonCreateDto(
                "João Teste",
                "123.456.789-00",
                LocalDate.of(1990, 1, 1),
                "11999999999",
                null
        );
        personCreateDto.setAddress(addressCreateDto);

        userCreateDto = new UserCreateDto(
                "joao@teste.com",
                "senha123",
                userTypeUuid,
                personCreateDto
        );

        // Criando entidades que serão retornadas pelos mocks
        address = new Address(
                addressCreateDto.getStreet(),
                addressCreateDto.getNumber(),
                addressCreateDto.getNeighborhood(),
                addressCreateDto.getCity(),
                addressCreateDto.getState(),
                addressCreateDto.getZipCode(),
                addressCreateDto.getComplement()
        );

        person = new Person(
                personCreateDto.getName(),
                personCreateDto.getCpf(),
                personCreateDto.getBirthDate(),
                personCreateDto.getPhone(),
                address
        );

        userType = new UserType(
                userTypeUuid,
                "ADMIN",
                "ADMIN",
                false,
                null,
                null
        );

        user = new User(
                userCreateDto.getEmail(),
                userCreateDto.getPassword(),
                userType,
                person
        );
    }

    @Test
    void createUser_Success() {
        // Arrange
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(userTypeRepository.findById(userTypeUuid)).thenReturn(Optional.of(userType));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User createdUser = userService.createUser(userCreateDto);

        // Assert
        assertNotNull(createdUser);
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getPerson().getName(), createdUser.getPerson().getName());
        assertEquals(user.getPerson().getAddress().getStreet(), createdUser.getPerson().getAddress().getStreet());
        assertEquals(user.getUserType().getUuid(), createdUser.getUserType().getUuid());
    }

    @Test
    void createUser_UserTypeNotFound() {
        // Arrange
        when(userTypeRepository.findById(userTypeUuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.createUser(userCreateDto));
    }

    @Test
    void createUser_ValidateAddressData() {
        // Arrange
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(userTypeRepository.findById(userTypeUuid)).thenReturn(Optional.of(userType));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User createdUser = userService.createUser(userCreateDto);

        // Assert
        Address savedAddress = createdUser.getPerson().getAddress();
        assertNotNull(savedAddress);
        assertEquals(address.getStreet(), savedAddress.getStreet());
        assertEquals(address.getNumber(), savedAddress.getNumber());
        assertEquals(address.getZipCode(), savedAddress.getZipCode());
    }

    @Test
    void createUser_ValidatePersonData() {
        // Arrange
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(userTypeRepository.findById(userTypeUuid)).thenReturn(Optional.of(userType));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User createdUser = userService.createUser(userCreateDto);

        // Assert
        Person savedPerson = createdUser.getPerson();
        assertNotNull(savedPerson);
        assertEquals(person.getName(), savedPerson.getName());
        assertEquals(person.getCpf(), savedPerson.getCpf());
        assertEquals(person.getBirthDate(), savedPerson.getBirthDate());
        assertEquals(person.getPhone(), savedPerson.getPhone());
    }

    @Test
    void createUser_EmailExists() {
        // Arrange
        when(userRepository.existsByEmail(any())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userCreateDto));
    }

    @Test
    void createUser_CpfExists() {
        // Arrange
        when(userRepository.existsUserByPerson_Cpf(any())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userCreateDto));
    }

    @Test
    void softDeleteUserByEmail_Success() {
        // Arrange
        String email = "joao@teste.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        userService.softDeleteUserByEmail(email);

        // Assert
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).softDeleteByEmail(email);
    }

    @Test
    void softDeleteUserByEmail_UserNotFound_ShouldThrow() {
        // Arrange
        String email = "naoexiste@teste.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.softDeleteUserByEmail(email));
    }

    @Test
    void updatePasswordByEmail_Success() {
        // Arrange
        String email = "joao@teste.com";
        UserUpdatePasswordDto dto = new UserUpdatePasswordDto("Nova@Senha1");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        userService.updatePasswordByEmail(email, dto);

        // Assert
        verify(userRepository).findByEmail(email);
        verify(userRepository).save(any(User.class));
    }

}