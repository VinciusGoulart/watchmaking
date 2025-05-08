package com.example.watchmaking.repository;

import com.example.watchmaking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    public Optional<User> findByEmail(String email);
    public boolean existsByEmail(String email);
    public boolean existsUserByPerson_Cpf(String cpf);
}
