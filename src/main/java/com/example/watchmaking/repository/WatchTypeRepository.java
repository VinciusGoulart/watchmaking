package com.example.watchmaking.repository;

import com.example.watchmaking.dto.watchType.ListWatchType;
import com.example.watchmaking.dto.watchType.SelectWatchType;
import com.example.watchmaking.entity.WatchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WatchTypeRepository extends JpaRepository<WatchType, UUID> {

    Optional<WatchType> findByCode(String code);

    @Query(value = """
                SELECT name, code
                FROM watch_types
                WHERE is_deleted = FALSE
                  AND (LOWER(name) LIKE LOWER(concat('%', :search, '%'))
                       OR LOWER(code) LIKE LOWER(concat('%', :search, '%')))
                LIMIT 40
            """, nativeQuery = true)
    List<SelectWatchType> selectBySearch(@Param("search") String search);

    @Query(value = """
                SELECT uuid, name, code, is_deleted
                FROM watch_types
                WHERE is_deleted = FALSE
                  AND (LOWER(name) LIKE LOWER(concat('%', :search, '%'))
                       OR LOWER(code) LIKE LOWER(concat('%', :search, '%')))
                ORDER BY created_at
            """,
            countQuery = """
                        SELECT COUNT(*) FROM watch_types
                        WHERE is_deleted = FALSE
                          AND (LOWER(name) LIKE LOWER(concat('%', :search, '%'))
                               OR LOWER(code) LIKE LOWER(concat('%', :search, '%')))
                    """,
            nativeQuery = true)
    Page<ListWatchType> listBySearch(@Param("search") String search, Pageable pageable);

    boolean existsByCodeIgnoreCase(String code);

    @Modifying
    @Query("UPDATE WatchType w SET w.isDeleted = true WHERE w.code = :code")
    void softDeleteByCode(@Param("code") String code);

    Optional<WatchType> findByUuid(UUID uuid);
}
