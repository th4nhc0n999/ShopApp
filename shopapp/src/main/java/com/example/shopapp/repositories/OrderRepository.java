package com.example.shopapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopapp.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
