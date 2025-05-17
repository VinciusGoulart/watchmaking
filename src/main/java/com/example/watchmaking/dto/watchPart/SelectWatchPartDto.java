package com.example.watchmaking.dto.watchPart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectWatchPartDto {
    private String name;
    private String code;
}
