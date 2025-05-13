package com.example.watchmaking.entity;
import com.example.watchmaking.dto.watchCategory.UpdateWatchCategoryDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;
@Entity
@Table(name = "watch_categories")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WatchCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public WatchCategory(String name, String code, Boolean isDeleted) {
        this.name = name;
        this.code = code;
        this.isDeleted = isDeleted;
    }

    public WatchCategory(UpdateWatchCategoryDto updateDto, WatchCategory watchCategory) {
        this.uuid = watchCategory.getUuid();
        this.name = updateDto.getName() != null ? updateDto.getName() : watchCategory.getName();
        this.code = updateDto.getCode() != null ? updateDto.getCode().toUpperCase(Locale.ROOT) : watchCategory.getCode();
        this.isDeleted = updateDto.getIsDeleted() != null ? updateDto.getIsDeleted() : watchCategory.getIsDeleted();
        this.createdAt = watchCategory.getCreatedAt();
        this.updatedAt = watchCategory.getUpdatedAt();
    }
}