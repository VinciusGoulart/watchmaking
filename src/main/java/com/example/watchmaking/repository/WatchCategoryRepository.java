package com.example.watchmaking.repository;

import com.example.watchmaking.dto.watchCategory.ListWatchCategory;
import com.example.watchmaking.dto.watchCategory.SelectWatchCategory;
import com.example.watchmaking.entity.WatchCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WatchCategoryRepository extends JpaRepository<WatchCategory, UUID>  {

    Optional<WatchCategory> findByCode(String code);

    @Query(value = """
                SELECT name, code
                FROM watch_categories
                WHERE is_deleted = FALSE
                  AND (LOWER(name) LIKE LOWER(concat('%', :search, '%'))
                       OR LOWER(code) LIKE LOWER(concat('%', :search, '%')))
                LIMIT 40
            """, nativeQuery = true)
    List<SelectWatchCategory> selectBySearch(@Param("search") String search);

    @Query(value = """
    SELECT uuid, name, code, is_deleted
    FROM watch_categories
    WHERE is_deleted = false
      AND (LOWER(name) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(code) LIKE LOWER(CONCAT('%', :search, '%')))
    ORDER BY created_at
""",
            countQuery = """
    SELECT COUNT(*) FROM watch_categories
    WHERE is_deleted = false
      AND (LOWER(name) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(code) LIKE LOWER(CONCAT('%', :search, '%')))
""",
            nativeQuery = true)
    Page<ListWatchCategory> listBySearch(@Param("search") String search, Pageable pageable);

    boolean existsByCodeIgnoreCase(String code);

    @Modifying
    @Query("UPDATE WatchCategory w SET w.isDeleted = true WHERE w.code = :code")
    void softDeleteByCode(@Param("code") String code);
}
