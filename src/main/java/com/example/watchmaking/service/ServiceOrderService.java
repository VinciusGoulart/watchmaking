package com.example.watchmaking.service;

import com.example.watchmaking.dto.serviceOrder.ServiceOrderCreateDto;
import com.example.watchmaking.dto.serviceOrder.ServiceOrderListDto;
import com.example.watchmaking.dto.serviceOrder.ServiceOrderUpdateDto;
import com.example.watchmaking.dto.serviceOrder.ServiceOrderViewDto;
import com.example.watchmaking.dto.serviceOrderItem.ServiceOrderItemViewDto;
import com.example.watchmaking.entity.*;
import com.example.watchmaking.repository.*;
import com.example.watchmaking.util.expcetions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class ServiceOrderService {
    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Autowired
    private ServiceOrderItemRepository serviceOrderItemRepository;

    @Autowired
    private WatchRepository watchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private WatchPartRepository watchPartRepository;

    @Transactional
    public void createServiceOrder(ServiceOrderCreateDto serviceOrderItem) {

        Watch watch = watchRepository.findByUuid(serviceOrderItem.getWatchUuid()).orElseThrow(
                () -> new NotFoundException("Relógio não encontrado!"));

        User technician = userRepository.findTechnicalByUuid(serviceOrderItem.getTechnicianUuid()).orElseThrow(
                () -> new NotFoundException("Técnico não encontrado!"));

        Person customer = personRepository.findByUuid(serviceOrderItem.getCustomerUuid()).orElseThrow(
                () -> new NotFoundException("Cliente não encontrado!"));

        ServiceOrder serviceOrder = new ServiceOrder(serviceOrderItem, customer, watch, technician);

        ServiceOrder savedServiceOrder = serviceOrderRepository.save(serviceOrder);

        if (serviceOrderItem.getOrderItems() != null) {
            serviceOrderItem.getOrderItems().forEach(item -> {
                WatchPart watchPart = watchPartRepository.findByUuid(item.getWatchPartUuid()).orElseThrow(
                        () -> new NotFoundException("Peça de relógio não encontrada!"));
                item.setServiceOrderUuid(savedServiceOrder.getUuid());
                ServiceOrderItem serviceOrderItemEntity = new ServiceOrderItem(
                        item.getQuantity(),
                        savedServiceOrder,
                        watchPart
                );

                serviceOrderItemRepository.save(serviceOrderItemEntity);
            });
        }
    }

    public ServiceOrderViewDto getServiceOrderByUuid(UUID uuid) {
        ServiceOrder serviceOrder = serviceOrderRepository.findWithDetailsByUuid(uuid).orElseThrow(
                () -> new NotFoundException("Ordem de serviço não encontrada!"));

        return new ServiceOrderViewDto(
                serviceOrder.getUuid().toString(),
                serviceOrder.getDescription(),
                serviceOrder.getCustomer().getName(),
                serviceOrder.getTechnician().getEmail(),
                serviceOrder.getServiceType(),
                serviceOrder.getStatus(),
                serviceOrder.getPrice(),
                serviceOrder.getEntryDate(),
                serviceOrder.getDeliveryDate(),
                serviceOrder.getNote(),
                serviceOrder.getCreatedAt(),
                serviceOrder.getUpdatedAt(),
                !serviceOrder.getOrderItems().isEmpty() ? serviceOrder.getOrderItems().stream()
                        .map(ServiceOrderItemViewDto::new).toList()
                        : null
        );
    }

    public Page<ServiceOrderListDto>  listServiceOrdersByFilters(
            Pageable pageable,
            String search,
            String serviceType,
            String status,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("A data inicial não pode ser posterior à data final");
        }

        LocalDateTime now = LocalDateTime.now();

        if (startDate == null && endDate == null) {
            startDate = now.minusDays(30);
            endDate = now.plusDays(30);
        }
        else if (startDate != null && endDate == null) {
            endDate = startDate.plusDays(60);
        }
        else if (startDate == null && endDate != null) {
            startDate = endDate.minusDays(60);
        }

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        if (daysBetween > 180) {
            throw new IllegalArgumentException("O intervalo máximo permitido é de 180 dias");
        }

        return serviceOrderRepository.listByFilters(pageable, search, serviceType,
                status, startDate.toString(), endDate.toString());
    }

    @Transactional
    public void updateServiceOrderByUuid(UUID uuid, ServiceOrderUpdateDto updateDto) {
        ServiceOrder existingServiceOrder = serviceOrderRepository.findByUuid(uuid).orElseThrow(
                () -> new NotFoundException("Ordem de serviço não encontrada!"));

        ServiceOrder newServiceOrder = new ServiceOrder(existingServiceOrder, updateDto);

        serviceOrderRepository.save(newServiceOrder);
    }

    @Transactional
    public void deleteServiceOrderByUuid(UUID uuid) {
        if (serviceOrderRepository.existsByUuid(uuid)) {
            throw new NotFoundException("Ordem de serviço não encontrada!");
        }
        ;

        serviceOrderRepository.deleteByUuid(uuid);
    }
}
