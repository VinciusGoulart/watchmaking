package com.example.watchmaking.service;

import com.example.watchmaking.dto.person.PersonUpdateDto;
import com.example.watchmaking.entity.Person;
import com.example.watchmaking.repository.AddressRepository;
import com.example.watchmaking.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService  {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    AddressRepository addressRepository;

    @Transactional
    public Person updatePerson(String email, PersonUpdateDto updateDto ) {
        Person person = personRepository.findByUserEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Usuario com o email " + email + " não  encontrado")
        );

        Person newPerson = new Person(updateDto, person);

        addressRepository.save(newPerson.getAddress());

        return personRepository.save(newPerson);
    }
}
