package com.example.watchmaking.entity;

import com.example.watchmaking.dto.address.AddressUpdateDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "uuid")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(nullable = false, length = 100)
    private String street;

    @Column(nullable = false, length = 20)
    private String number;

    @Column(nullable = false, length = 100)
    private String neighborhood;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(name = "zip_code", nullable = false, length = 20)
    private String zipCode;

    @Column(length = 100)
    private String complement;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Address(String street, String number, String neighborhood, String city, String state, String zipCode, String complement) {
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.complement = complement;
    }

    public Address (AddressUpdateDto  addressUpdateDto, Address address) {
        this.uuid = address.getUuid();
        this.street = addressUpdateDto.getStreet() != null ? addressUpdateDto.getStreet() : address.getStreet();
        this.number = addressUpdateDto.getNumber() != null ? addressUpdateDto.getNumber() : address.getNumber();
        this.neighborhood = addressUpdateDto.getNeighborhood() != null ? addressUpdateDto.getNeighborhood() : address.getNeighborhood();
        this.city = addressUpdateDto.getCity() != null ? addressUpdateDto.getCity() : address.getCity();
        this.state = addressUpdateDto.getState() != null ? addressUpdateDto.getState() : address.getState();
        this.zipCode = addressUpdateDto.getZipCode() != null ? addressUpdateDto.getZipCode() : address.getZipCode();
        this.complement = addressUpdateDto.getComplement() != null ? addressUpdateDto.getComplement() : address.getComplement();
    }
}
