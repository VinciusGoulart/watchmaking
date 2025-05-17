package com.example.watchmaking.dto.serviceOrderItem;

import com.example.watchmaking.entity.ServiceOrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderItemViewDto {

    private UUID uuid;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal subtotal;
    private UUID watchPartUuid;

    public ServiceOrderItemViewDto(List<ServiceOrderItem> orderItems) {
        for (ServiceOrderItem item : orderItems) {
            this.uuid = item.getUuid();
            this.unitPrice = item.getUnitPrice();
            this.quantity = item.getQuantity();
            this.subtotal = item.getSubtotal();
            this.watchPartUuid = item.getWatchPart().getUuid();
        }
    }
}
