package com.example.watchmaking.repository;

import com.example.watchmaking.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, UUID> {
}
