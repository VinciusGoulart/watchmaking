package com.example.watchmaking.repository;

import com.example.watchmaking.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {


    @Query("""
    SELECT p FROM Person p
    JOIN FETCH p.address
    JOIN User u ON u.person = p
    WHERE u.email = :email
""")
    Optional<Person> findByUserEmail(@Param("email") String email);

}
