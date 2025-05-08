package com.example.watchmaking.entity;

import com.example.watchmaking.dto.person.PersonUpdateDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "persons")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "uuid")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 15)
    private String cpf;

    @PastOrPresent
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, length = 100)
    private String phone;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_uuid")
    private Address address;

    public Person(String name, String cpf, LocalDate birthDate, String phone, Address address) {
        this.name = name;
        this.cpf = cpf;
        this.birthDate = birthDate;
        this.phone = phone;
        this.address = address;
    }

    public Person(PersonUpdateDto updateDto, Person person) {
        this.uuid = person.getUuid();
        this.name = updateDto.getName() != null ?  updateDto.getName() : person.getName();
        this.cpf = updateDto.getCpf() != null ?  updateDto.getCpf() : person.getCpf();
        this.birthDate = updateDto.getBirthDate() != null ?  updateDto.getBirthDate() : person.getBirthDate();
        this.phone = updateDto.getPhone() != null ? updateDto.getPhone() : person.getPhone();
        this.address= updateDto.getAddress() != null ? new Address(updateDto.getAddress(),person.getAddress()) : person.getAddress();
    }
}
