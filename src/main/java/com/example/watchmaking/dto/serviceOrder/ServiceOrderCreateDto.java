package com.example.watchmaking.dto.serviceOrder;

import com.example.watchmaking.dto.serviceOrderItem.ServiceOrderItemCreateDto;
import com.example.watchmaking.util.enums.ServiceStatus;
import com.example.watchmaking.util.enums.ServiceType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceOrderCreateDto {

    @NotBlank
    private String description;

    @NotNull
    private ServiceType serviceType;

    @NotNull
    private ServiceStatus status;

    @Positive
    private BigDecimal price;

    private LocalDateTime entryDate = LocalDateTime.now();

    @Future(message = "Data de entrega deve ser no futuro")
    private LocalDateTime deliveryDate;

    private String note;

    @NotBlank
    private UUID customerUuid;

    @NotBlank
    private UUID watchUuid;

    @NotBlank
    private UUID technicianUuid;

    private List<@Valid ServiceOrderItemCreateDto> orderItems;
}
