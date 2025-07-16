package com.example.website.repository;


import com.example.website.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /** All cart items that belong to a given user. */
    List<CartItem> findByUserId(Long userId);

    /** Remove entire cart for a given user (used on checkout / clear cart). */
    void deleteByUserId(Long userId);
}

