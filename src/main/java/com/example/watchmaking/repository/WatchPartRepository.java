package com.example.watchmaking.repository;

import com.example.watchmaking.dto.watchPart.ListWatchPartDto;
import com.example.watchmaking.dto.watchPart.SelectWatchPartDto;
import com.example.watchmaking.entity.WatchPart;
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
public interface WatchPartRepository extends JpaRepository<WatchPart, UUID>  {

    Optional<WatchPart> findByCode(String code);

    @Query(value = """
                SELECT name, code
                FROM watch_parts
                WHERE is_deleted = FALSE
                  AND (LOWER(name) LIKE LOWER(concat('%', :search, '%'))
                       OR LOWER(code) LIKE LOWER(concat('%', :search, '%')))
                LIMIT 40
            """, nativeQuery = true)
    List<SelectWatchPartDto> selectBySearch(@Param("search") String search);

    @Query(value = """
    SELECT uuid, name, code, is_deleted
    FROM watch_parts
    WHERE is_deleted = false
      AND (LOWER(name) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(code) LIKE LOWER(CONCAT('%', :search, '%')))
    ORDER BY created_at
""",
            countQuery = """
    SELECT COUNT(*) FROM watch_parts
    WHERE is_deleted = false
      AND (LOWER(name) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(code) LIKE LOWER(CONCAT('%', :search, '%')))
""",
            nativeQuery = true)
    Page<ListWatchPartDto> listBySearch(@Param("search") String search, Pageable pageable);

    boolean existsByCodeIgnoreCase(String code);

    @Modifying
    @Query("UPDATE WatchPart w SET w.isDeleted = true WHERE w.code = :code")
    void softDeleteByCode(@Param("code") String code);

    Optional<WatchPart> findByUuid(UUID uuid);
}
