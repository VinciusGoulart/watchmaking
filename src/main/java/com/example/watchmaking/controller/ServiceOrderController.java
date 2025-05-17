package com.example.watchmaking.controller;

import com.example.watchmaking.dto.serviceOrder.ServiceOrderCreateDto;
import com.example.watchmaking.dto.serviceOrder.ServiceOrderListDto;
import com.example.watchmaking.dto.serviceOrder.ServiceOrderUpdateDto;
import com.example.watchmaking.dto.serviceOrder.ServiceOrderViewDto;
import com.example.watchmaking.entity.ServiceOrder;
import com.example.watchmaking.service.ServiceOrderService;
import com.example.watchmaking.util.enums.ServiceStatus;
import com.example.watchmaking.util.enums.ServiceType;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("service-orders")
public class ServiceOrderController {
    @Autowired
    private ServiceOrderService serviceOrderService;

    @PostMapping
    public ResponseEntity<ServiceOrder> createServiceOrder(@Valid @RequestBody ServiceOrderCreateDto createServiceOrderDto) {
        serviceOrderService.createServiceOrder(createServiceOrderDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<ServiceOrderViewDto> findByUuid(@PathVariable UUID uuid) {
        ServiceOrderViewDto serviceOrder = serviceOrderService.getServiceOrderByUuid(uuid);
        return ResponseEntity.ok().body(serviceOrder);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ServiceOrderListDto>> listWatchCategories(
            @PageableDefault(size = 20, sort = "delivery_date", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false,defaultValue = "") String search,
            @RequestParam(required = false) ServiceType serviceType,
            @RequestParam(required = false) ServiceStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        Page<ServiceOrderListDto> result = serviceOrderService.listServiceOrdersByFilters(
                pageable,
                search,
                serviceType.toString(),
                status.toString(),
                startDate,
                endDate
        );
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/update/{uuid}")
    public ResponseEntity<ServiceOrder> updateServiceOrder(@PathVariable UUID uuid, @Valid @RequestBody ServiceOrderUpdateDto updateDto) {
        serviceOrderService.updateServiceOrderByUuid(uuid, updateDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/uuid/{uuid}")
    public ResponseEntity<String> deleteServiceOrder(@PathVariable UUID uuid) {
        serviceOrderService.deleteServiceOrderByUuid(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
