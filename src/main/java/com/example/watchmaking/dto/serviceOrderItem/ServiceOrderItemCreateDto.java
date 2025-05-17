package com.example.watchmaking.dto.serviceOrderItem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderItemCreateDto {

    @Positive
    private Integer quantity;

    private UUID serviceOrderUuid;

    @NotNull
    private UUID watchPartUuid;
}
