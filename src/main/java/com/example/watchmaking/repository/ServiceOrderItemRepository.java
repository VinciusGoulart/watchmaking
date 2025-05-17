package com.example.watchmaking.repository;

import com.example.watchmaking.entity.ServiceOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServiceOrderItemRepository extends JpaRepository<ServiceOrderItem, UUID> {
}
