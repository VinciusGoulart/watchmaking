package com.example.watchmaking.dto.watchType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListWatchType {
    private UUID uuid;
    private String name;
    private String code;
    private Boolean isDeleted;
}
