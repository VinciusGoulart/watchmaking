package com.example.watchmaking.repository;

import com.example.watchmaking.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StorageRepository extends JpaRepository<Storage, UUID> {
    Optional<Storage> findByUuid(UUID uuid);
}
