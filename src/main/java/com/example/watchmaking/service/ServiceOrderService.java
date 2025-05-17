package com.example.watchmaking.service;

import com.example.watchmaking.dto.serviceOrder.ServiceOrderCreateDto;
import com.example.watchmaking.dto.serviceOrder.ServiceOrderViewDto;
import com.example.watchmaking.dto.serviceOrderItem.ServiceOrderItemViewDto;
import com.example.watchmaking.entity.*;
import com.example.watchmaking.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public ServiceOrder createServiceOrder(ServiceOrderCreateDto serviceOrderItem) {

        Watch watch = watchRepository.findByUuid(serviceOrderItem.getWatchUuid()).orElseThrow(
                () -> new RuntimeException("Relógio não encontrado!"));

        User technician = userRepository.findTechnicalByUuid(serviceOrderItem.getTechnicianUuid()).orElseThrow(
                () -> new RuntimeException("Técnico não encontrado!"));

        Person customer = personRepository.findByUuid(serviceOrderItem.getCustomerUuid()).orElseThrow(
                () -> new RuntimeException("Cliente não encontrado!"));

        ServiceOrder serviceOrder = new ServiceOrder(serviceOrderItem, customer, watch, technician);

        ServiceOrder savedServiceOrder = serviceOrderRepository.save(serviceOrder);

        if (serviceOrderItem.getOrderItems() != null) {
            serviceOrderItem.getOrderItems().forEach(item -> {
                WatchPart watchPart = watchPartRepository.findByUuid(item.getWatchPartUuid()).orElseThrow(
                        () -> new RuntimeException("Peça de relógio não encontrada!"));
                item.setServiceOrderUuid(savedServiceOrder.getUuid());
                ServiceOrderItem serviceOrderItemEntity = new ServiceOrderItem(
                        item.getQuantity(),
                        savedServiceOrder,
                        watchPart
                );

                serviceOrderItemRepository.save(serviceOrderItemEntity);
            });
        }

        return savedServiceOrder;
    }

    public ServiceOrderViewDto getServiceOrderByUuid(UUID uuid) {
        ServiceOrder serviceOrder = serviceOrderRepository.findWithDetailsByUuid((uuid)).orElseThrow(
                () -> new RuntimeException("Ordem de serviço não encontrada!"));

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
                        .map(item -> new ServiceOrderItemViewDto(serviceOrder.getOrderItems())).toList()
                        : null
        );
    }
}
