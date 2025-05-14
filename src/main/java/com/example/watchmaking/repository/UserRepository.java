package com.example.watchmaking.repository;

import com.example.watchmaking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    public Optional<User> findByEmail(String email);

    public boolean existsByEmail(String email);

    public boolean existsUserByPerson_Cpf(String cpf);

    @Modifying
    @Query("UPDATE User u SET u.isDeleted = true WHERE u.email = :email")
    public void softDeleteByEmail(@Param("email") String email);
}
