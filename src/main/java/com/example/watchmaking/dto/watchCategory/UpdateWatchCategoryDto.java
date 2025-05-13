package com.example.watchmaking.dto.watchCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class UpdateWatchCategoryDto {
    @Length(min=2, max=100)
    private String name;

    @Length(min=2, max=50)
    private String code;

    private Boolean isDeleted;
}
