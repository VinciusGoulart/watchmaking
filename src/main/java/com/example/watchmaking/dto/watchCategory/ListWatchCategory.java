package com.example.watchmaking.dto.watchCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListWatchCategory {
    private UUID uuid;
    private String name;
    private String code;
    private Boolean isDeleted;
}
