package com.example.watchmaking.repository;

import com.example.watchmaking.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BrandRepository extends JpaRepository<Brand, UUID> {
    boolean existsByName(String name);

    boolean existsByUuidAndIsDeletedFalse(UUID uuid);

    @Modifying
    @Query(value = "UPDATE Brand b SET b.isDeleted = true WHERE b.uuid = :uuid")
    void deleteByUuid(@Param("uuid") UUID uuid);

    boolean existsByUuid(UUID uuid);

    Optional<Brand> findByUuid(UUID uuid);
}
