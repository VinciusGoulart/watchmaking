package com.example.watchmaking.dto.watchPart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListWatchPartDto {
    private UUID uuid;
    private String name;
    private String code;
    private Boolean isDeleted;
}
