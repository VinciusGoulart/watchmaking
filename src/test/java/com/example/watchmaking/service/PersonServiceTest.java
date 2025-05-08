package com.example.watchmaking.service;

import com.example.watchmaking.dto.address.AddressUpdateDto;
import com.example.watchmaking.dto.person.PersonUpdateDto;
import com.example.watchmaking.entity.Address;
import com.example.watchmaking.entity.Person;
import com.example.watchmaking.repository.AddressRepository;
import com.example.watchmaking.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private PersonService personService;

    private Person person;
    private Address address;

    @BeforeEach
    void setup() {
        address = new Address();
        address.setUuid(UUID.randomUUID());
        address.setStreet("Rua antiga");

        person = new Person();
        person.setUuid(UUID.randomUUID());
        person.setName("Nome antigo");
        person.setCpf("123.456.789-00");
        person.setBirthDate(LocalDate.of(1990, 1, 1));
        person.setPhone("11999999999");
        person.setAddress(address);
    }

    @Test
    void updatePerson_Success() {
        // Arrange
        String email = "joao@email.com";

        AddressUpdateDto addressDto = new AddressUpdateDto("Nova Rua", null, null, null, null, null, null);
        PersonUpdateDto dto = new PersonUpdateDto("Novo Nome", null, null, null, addressDto);

        when(personRepository.findByUserEmail(email)).thenReturn(Optional.of(person));
        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Person updated = personService.updatePerson(email, dto);

        // Assert
        assertEquals("Novo Nome", updated.getName());
        assertEquals("Nova Rua", updated.getAddress().getStreet());

        verify(personRepository).findByUserEmail(email);
        verify(addressRepository).save(any(Address.class));
        verify(personRepository).save(any(Person.class));
    }

    @Test
    void updatePerson_UserNotFound_ShouldThrow() {
        // Arrange
        String email = "naoexiste@email.com";
        when(personRepository.findByUserEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> personService.updatePerson(email, new PersonUpdateDto()));
    }
}
