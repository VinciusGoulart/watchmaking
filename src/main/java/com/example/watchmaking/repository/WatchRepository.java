package com.example.watchmaking.repository;

import com.example.watchmaking.entity.Watch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WatchRepository extends JpaRepository<Watch, UUID> {

    Optional<Watch> findByUuid(UUID id);

    boolean existsByUuid(UUID uuid);

    @Modifying
    @Query("UPDATE Watch w SET w.isDeleted = true WHERE w.uuid = :uuid")
    void softDeleteByUuid(@Param("uuid") UUID uuid);
}
