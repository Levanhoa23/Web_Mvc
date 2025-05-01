package com.example.levanhoa.repository;

import com.example.levanhoa.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    OrderItem findById(int id);
    List<OrderItem> findByOrderId(int orderId);
}