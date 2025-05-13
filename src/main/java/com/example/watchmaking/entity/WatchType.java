package com.example.watchmaking.entity;

import com.example.watchmaking.dto.watchType.UpdateWatchTypeDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name = "watch_types")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WatchType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code; // ex: ANALOG, DIGITAL

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public WatchType(String name, String code, Boolean isDeleted) {
        this.name = name;
        this.code = code;
        this.isDeleted = isDeleted;
    }

    public WatchType(UpdateWatchTypeDto updateDto, WatchType watchCategory) {
        this.uuid = watchCategory.getUuid();
        this.name = updateDto.getName() != null ? updateDto.getName() : watchCategory.getName();
        this.code = updateDto.getCode() != null ? updateDto.getCode().toUpperCase(Locale.ROOT) : watchCategory.getCode();
        this.isDeleted = updateDto.getIsDeleted() != null ? updateDto.getIsDeleted() : watchCategory.getIsDeleted();
        this.createdAt = watchCategory.getCreatedAt();
        this.updatedAt = watchCategory.getUpdatedAt();
    }
}
