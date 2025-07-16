package com.example.website.repository;



import com.example.website.*;
import com.example.website.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /** Retrieve all orders placed by a specific user. */
    List<Order> findByUserId(Long userId);
}
