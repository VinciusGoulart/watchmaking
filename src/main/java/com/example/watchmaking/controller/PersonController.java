package com.example.watchmaking.controller;

import com.example.watchmaking.dto.person.PersonUpdateDto;
import com.example.watchmaking.entity.Person;
import com.example.watchmaking.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("persons")
public class PersonController {
    @Autowired
    private PersonService personService;

    @PatchMapping("/update/{email}")
    public ResponseEntity<Person> update(@PathVariable("email") String email,@Valid @RequestBody PersonUpdateDto updateDto) {
        Person person = personService.updatePerson(email, updateDto);

        return ResponseEntity.ok(person);
    }
}
